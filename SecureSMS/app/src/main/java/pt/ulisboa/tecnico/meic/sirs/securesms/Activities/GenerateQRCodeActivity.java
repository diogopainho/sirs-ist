package pt.ulisboa.tecnico.meic.sirs.securesms.Activities;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Base64;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import pt.ulisboa.tecnico.meic.sirs.securesms.Contents;
import pt.ulisboa.tecnico.meic.sirs.securesms.QRCodeEncoder;
import pt.ulisboa.tecnico.meic.sirs.securesms.R;

public class GenerateQRCodeActivity extends Activity {

    private static final String TAG = GenerateQRCodeActivity.class.getSimpleName();

    @InjectView(R.id.name)
    TextView nameLabel;
    @InjectView(R.id.phone)
    TextView phoneLabel;
    @InjectView(R.id.key)
    TextView keyLabel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_qrcode);
        ButterKnife.inject(this);
        generateQRCode(getIntent().getStringExtra("NAME"), getIntent().getStringExtra("PHONE_NUMBER"), getIntent().getByteArrayExtra("PUB_KEY"));

    }

    public void generateQRCode(String name, String phonenumber, byte[] publickey){
        String qrInputText = name+";"+phonenumber+";"+Base64.encodeToString(publickey, Base64.DEFAULT);

        nameLabel.setText("Name: " + name);
        phoneLabel.setText("Phone Number: " + phonenumber);
        keyLabel.setText("Public Key:" + Base64.encodeToString(publickey, Base64.DEFAULT));

        //Find screen size
        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        int height = point.y;
        int smallerDimension = width < height ? width : height;
        smallerDimension = smallerDimension * 3/4;

        //Encode with a QR Code image
        QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(qrInputText,
                null,
                Contents.Type.TEXT,
                BarcodeFormat.QR_CODE.toString(),
                smallerDimension);
        try {
            Bitmap bitmap = qrCodeEncoder.encodeAsBitmap();
            ImageView myImage = (ImageView) findViewById(R.id.imageView1);
            myImage.setImageBitmap(bitmap);

        } catch (WriterException e) {
            e.printStackTrace();
        }

    }

}

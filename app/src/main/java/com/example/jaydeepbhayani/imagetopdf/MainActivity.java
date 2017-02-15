package com.example.jaydeepbhayani.imagetopdf;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.itextpdf.text.BadElementException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int SELECT_PICTURE = 100;
    Button Converter;
    private static String FILE = "";
    Document document;
    File pdfFile;
    static Image image;
    Bitmap bmp;
    static Bitmap bt;
    static byte[] bArray;
    private EditText etPages;
    Spinner spOrientation, spPageSize;
    private ArrayList<String> singleDouble;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Converter = (Button) findViewById(R.id.converter);
        spOrientation = (Spinner) findViewById(R.id.sp_orientation);
        spPageSize = (Spinner) findViewById(R.id.sp_page_size);

       /* //singleDouble = new ArrayList<String>();
        singleDouble.add("Portrait");
        singleDouble.add("LandScap");*/

        etPages = (EditText) findViewById(R.id.et_pages);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, new String[]{"Portrait", "LandScap"});
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, new String[]{"A0", "A1", "A2", "A3", "A4", "A5", "A6", "A7", "A8"});
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spOrientation.setAdapter(dataAdapter);
        spPageSize.setAdapter(dataAdapter2);

        //First Check if the external storage is writable
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {

            //Create a directory for your PDF
            File pdfDir = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOCUMENTS), "MyApp");
            if (!pdfDir.exists()) {
                pdfDir.mkdir();
            }

            //Now create the name of your PDF file that you will generate
            pdfFile = new File(pdfDir, "myPdfFile.pdf");
        } else {
            //Create a directory for your PDF
            File pdfDir = new File(Environment.getDownloadCacheDirectory(), "MyApp");
            if (!pdfDir.exists()) {
                pdfDir.mkdir();
            }

            //Now create the name of your PDF file that you will generate
            pdfFile = new File(pdfDir, "myPdfFile.pdf");
        }


        FILE = pdfFile.getAbsolutePath();

        Converter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    spOrientation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                            String spOrientationValue = spOrientation.getSelectedItem().toString();

                            Log.d("Value", spOrientationValue);

                            switch (spOrientationValue) {
                                case "Portrait":
                                    document = new Document(PageSize.LETTER);
                                    PortraitSizes();
                                    break;
                                case "LandScap":
                                    document = new Document(PageSize.LETTER.rotate());
                                    LandscapSizes();
                                    break;
                                default:
                                    document = new Document(PageSize.LETTER);
                                    break;
                            }

                        }

                        public void onNothingSelected(
                                AdapterView<?> adapterView) {

                        }
                    });


                    PdfWriter writer = PdfWriter.getInstance(document,
                            new FileOutputStream(FILE));
                    document.open();
                    InputStream is = getResources().openRawResource(R.raw.fiftyshadesofgrey);
                    PdfReader reader = new PdfReader(is);

                /*   java.util.List<Integer> ll= new ArrayList<Integer>();
                    ll.add(1);
                    ll.add(5);*/
                    reader.selectPages(etPages.getText().toString());

                    int n = reader.getNumberOfPages();
                    PdfImportedPage page;
                    // Go through all pages
                    for (int i = 1; i <= n; i++) {

                        page = writer.getImportedPage(reader, i);
                        Image instance = Image.getInstance(page);

                        float documentWidth = document.getPageSize().getWidth() - document.leftMargin() - document.rightMargin();
                        float documentHeight = document.getPageSize().getHeight() - document.topMargin() - document.bottomMargin();
                        instance.scaleToFit(documentWidth, documentHeight);
                        // here you can show image on your phone
                        document.add(instance);
                    }
                    document.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void PortraitSizes() {
        spPageSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String spPageSizeValue = spPageSize.getSelectedItem().toString();

                Log.d("Value", spPageSizeValue);

                switch (spPageSizeValue) {
                    case "A0":
                        document = new Document(PageSize.A0);
                        break;
                    case "A1":
                        document = new Document(PageSize.A1);
                        break;
                    case "A2":
                        document = new Document(PageSize.A2);
                        break;
                    case "A3":
                        document = new Document(PageSize.A3);
                        break;
                    case "A4":
                        document = new Document(PageSize.A4);
                        break;
                    case "A5":
                        document = new Document(PageSize.A5);
                        break;
                    case "A6":
                        document = new Document(PageSize.A6);
                        break;
                    case "A7":
                        document = new Document(PageSize.A7);
                        break;
                    case "A8":
                        document = new Document(PageSize.A8);
                        break;
                    default:
                        document = new Document(PageSize.A4);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void LandscapSizes() {
        spPageSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String spPageSizeValue = spPageSize.getSelectedItem().toString();

                Log.d("Value", spPageSizeValue);

                switch (spPageSizeValue) {
                    case "A0":
                        document = new Document(PageSize.A0.rotate());
                        break;
                    case "A1":
                        document = new Document(PageSize.A1.rotate());
                        break;
                    case "A2":
                        document = new Document(PageSize.A2.rotate());
                        break;
                    case "A3":
                        document = new Document(PageSize.A3.rotate().rotate());
                        break;
                    case "A4":
                        document = new Document(PageSize.A4.rotate());
                        break;
                    case "A5":
                        document = new Document(PageSize.A5.rotate());
                        break;
                    case "A6":
                        document = new Document(PageSize.A6.rotate());
                        break;
                    case "A7":
                        document = new Document(PageSize.A7.rotate());
                        break;
                    case "A8":
                        document = new Document(PageSize.A8.rotate());
                        break;
                    default:
                        document = new Document(PageSize.A4.rotate());
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private static void addImage(Document document) {

        try {

            image = Image.getInstance(bArray); ///Here i set byte array..you can do bitmap to byte array and set in image...
            float documentWidth = document.getPageSize().getWidth() - document.leftMargin() - document.rightMargin();
            float documentHeight = document.getPageSize().getHeight() - document.topMargin() - document.bottomMargin();
            image.scaleToFit(documentWidth, documentHeight);
        } catch (BadElementException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // image.scaleAbsolute(150f, 150f);
        try {
            document.add(image);
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
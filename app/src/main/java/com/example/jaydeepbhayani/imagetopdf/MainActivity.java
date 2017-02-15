package com.example.jaydeepbhayani.imagetopdf;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.itextpdf.text.BadElementException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.io.RandomAccessSourceFactory;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.RandomAccessFileOrArray;

import org.docx4j.Docx4J;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.net.MalformedURLException;
import java.nio.ByteBuffer;

public class MainActivity extends AppCompatActivity {

    private static final int SELECT_PICTURE = 100;
    Button Converter;
    private static String INPUTFILE = "storage/sdcard0/FirstPdf.pdf";
    private static String FILE = "storage/sdcard0/FirstPdf.pdf";
    private static String OUTPUTFILE = "R.raw.asdf";

    File pdfFile;
    static Image image;
    Bitmap bmp;
    static Bitmap bt;
    static byte[] bArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Converter = (Button) findViewById(R.id.converter);
        WordprocessingMLPackage wordMLPackage = new WordprocessingMLPackage();
        try {
            wordMLPackage =
                    Docx4J.load(new File(FILE));
        } catch (Docx4JException e) {
            e.printStackTrace();
        }

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
                    /*PdfReader reader = new PdfReader(String.valueOf(R.raw.asdf));

                    Document document = new Document(PageSize.A4.rotate());

                   PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(FILE));
                    PdfImportedPage page;
                    document.open();

                    for(int i = 0 ; i< reader.getNumberOfPages(); i++)
                    {
                        page = writer.getImportedPage(reader,i);

                        Image image = Image.getInstance(page);

                        document.add(image);
                    }

                   *//* bmp = BitmapFactory.decodeResource(getResources(),R.raw.asdf);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    bArray = stream.toByteArray();
                    addImage(document);*//*

                  *//*  bmp = BitmapFactory.decodeResource(getResources(),R.drawable.b);
                    stream = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    bArray = stream.toByteArray();
                    addImage(document);

                    bmp = BitmapFactory.decodeResource(getResources(),R.drawable.c);
                    stream = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    bArray = stream.toByteArray();
                    addImage(document);*//*

                    document.close();*/
                    Document document = new Document(PageSize.LETTER.rotate());

                    PdfWriter writer = PdfWriter.getInstance(document,
                            new FileOutputStream(FILE));
                    document.open();
                    PdfReader reader = new PdfReader(INPUTFILE);
                    int n = reader.getNumberOfPages();
                    PdfImportedPage page;
                    // Go through all pages
                    for (int i = 1; i <= n; i++) {
                        // Only page number 2 will be included
                        if (i == 2) {
                            page = writer.getImportedPage(reader, i);
                            //Image instance = Image.getInstance(page);
                            // here you can show image on your phone
                            document.add((Element) page);
                        }
                    }
                    document.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }
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
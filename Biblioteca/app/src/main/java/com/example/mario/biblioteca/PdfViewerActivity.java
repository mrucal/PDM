package com.example.mario.biblioteca;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;

public class PdfViewerActivity extends AppCompatActivity {

    PDFView pdfView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);

        Intent i = getIntent();
        String path = i.getStringExtra("path");
        pdfView = (PDFView)findViewById(R.id.pdfView);
        pdfView.fromFile(new File(path)).load();
    }
}

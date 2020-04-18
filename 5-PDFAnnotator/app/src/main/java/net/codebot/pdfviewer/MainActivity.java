package net.codebot.pdfviewer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfRenderer;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

// PDF sample code from
// https://medium.com/@chahat.jain0/rendering-a-pdf-document-in-android-activity-fragment-using-pdfrenderer-442462cb8f9a
// Issues about cache etc. are not at all obvious from documentation, so we should expect people to need this.
// We may wish to provied them with this code.

public class MainActivity extends AppCompatActivity {

    final String LOGNAME = "pdf_viewer";
    final String FILENAME = "shannon1948.pdf";
    final int FILERESID = R.raw.shannon1948;
    enum ClickerState {NORMAL, DRAW, HIGHLIGHT, ERASE};
    ClickerState clickerState;
    Model model;

    // manage the pages of the PDF, see below
    PdfRenderer pdfRenderer;
    private ParcelFileDescriptor parcelFileDescriptor;
    private PdfRenderer.Page currentPage;

    // custom ImageView class that captures strokes and draws them over the image
    PDFimage pageImage;
    int currentPageNumber, currentPageCount;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void openPdf() {
            pdfRenderer = null;
            LinearLayout layout = findViewById(R.id.pdfLayout);
            pageImage = new PDFimage(this, clickerState);
            layout.removeAllViews();
            layout.addView(pageImage);
            layout.setEnabled(true);
            pageImage.setMinimumWidth(1000);
            pageImage.setMinimumHeight(1850);
            try {
                openRenderer(this);
            } catch (Exception ex) {
                Log.d(LOGNAME, "Problem opening renderer: " + ex.toString());
            }
            try {
                showPage(currentPageNumber);
            } catch (Exception ex) {
                Log.d(LOGNAME, "Problem showing the page using the renderer: " + ex.toString());
            }
        try {
            closeRenderer();
        } catch (Exception ex) {
            Log.d(LOGNAME, "Problem closing renderer: " + ex.toString());
        }
        model.setNumPages(currentPageCount);
        pageImage.setPageIndex(currentPageNumber);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        clickerState = ClickerState.NORMAL;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setTitle(FILENAME);
        currentPageNumber = 0;
        model = Model.getInstance();
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView documentTitle = findViewById(R.id.documentTitle);
        documentTitle.setText(FILENAME);

        // open page 0 of the PDF
        // it will be displayed as an image in the pageImage (above)
        openPdf();
        final TextView pageNumber = findViewById(R.id.pageNumber);
        pageNumber.setText("Page " + Integer.toString(currentPageNumber + 1) + "/" + Integer.toString(currentPageCount));
        ((ImageButton)findViewById(R.id.up)).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                if (currentPageNumber > 0) {
                    --currentPageNumber;
                    openPdf();
                    TextView pageNumber = findViewById(R.id.pageNumber);
                    pageNumber.setText("Page " + Integer.toString(currentPageNumber + 1) + "/" + Integer.toString(currentPageCount));
                }
            }
        });
        ((ImageButton)findViewById(R.id.down)).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                    if (currentPageNumber < currentPageCount - 1) {
                        ++currentPageNumber;
                        openPdf();
                        TextView pageNumber = findViewById(R.id.pageNumber);
                        pageNumber.setText("Page " + Integer.toString(currentPageNumber + 1) + "/" + Integer.toString(currentPageCount));
                    }
            }
        });
        ((ImageButton)findViewById(R.id.draw)).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                clickerState = ClickerState.DRAW;
                pageImage.setClickerState(clickerState);
            }
        });
        ((ImageButton)findViewById(R.id.highlight)).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                clickerState = ClickerState.HIGHLIGHT;
                pageImage.setClickerState(clickerState);
            }
        });
        ((ImageButton)findViewById(R.id.erase)).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                clickerState = ClickerState.ERASE;
                pageImage.setClickerState(clickerState);
            }
        });
        ((ImageButton)findViewById(R.id.undoButton)).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                PossibleAction action = model.undo();
                if (action != null) {
                    if (action.pageNumber != currentPageNumber) {
                        currentPageNumber = action.pageNumber;
                        openPdf();
                        TextView pageNumber = findViewById(R.id.pageNumber);
                        pageNumber.setText("Page " + Integer.toString(currentPageNumber + 1) + "/" + Integer.toString(currentPageCount));
                    }
                    pageImage.unperformAction(action);
                }
            }
        });
        ((ImageButton)findViewById(R.id.redoButton)).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                PossibleAction action = model.redo();
                if (action != null) {
                    if (action.pageNumber != currentPageNumber) {
                        currentPageNumber = action.pageNumber;
                        openPdf();
                        TextView pageNumber = findViewById(R.id.pageNumber);
                        pageNumber.setText("Page " + Integer.toString(currentPageNumber + 1) + "/" + Integer.toString(currentPageCount));
                    }
                    pageImage.performAction(action);
                }
            }
        });
        ((Toolbar)findViewById(R.id.toolbar)).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                clickerState = ClickerState.NORMAL;
                pageImage.setClickerState(clickerState);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onStop() {
        super.onStop();
        try {
            closeRenderer();
        } catch (IOException ex) {
            Log.d(LOGNAME, "Unable to close PDF renderer");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void openRenderer(Context context) throws IOException {
        // In this sample, we read a PDF from the assets directory.
        File file = new File(context.getCacheDir(), FILENAME);
        if (!file.exists()) {
            // pdfRenderer cannot handle the resource directly,
            // so extract it into the local cache directory.
            InputStream asset = this.getResources().openRawResource(FILERESID);
            FileOutputStream output = new FileOutputStream(file);
            final byte[] buffer = new byte[1024];
            int size;
            while ((size = asset.read(buffer)) != -1) {
                output.write(buffer, 0, size);
            }
            asset.close();
            output.close();
        }
        parcelFileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);

        // capture PDF data
        // all this just to get a handle to the actual PDF representation
        if (parcelFileDescriptor != null) {
            pdfRenderer = new PdfRenderer(parcelFileDescriptor);
        }
    }

    // do this before you quit!
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void closeRenderer() throws IOException {
        if (null != currentPage) {
            currentPage.close();
            currentPage = null;
        }
        pdfRenderer.close();
        parcelFileDescriptor.close();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void showPage(int index) {
        currentPageCount = pdfRenderer.getPageCount();
        if (currentPageCount <= index) {
            return;
        }
        // Close the current page before opening another one.
        if (null != currentPage) {
            currentPage.close();
            currentPage = null;
        }
        // Use `openPage` to open a specific page in PDF.
        currentPage = pdfRenderer.openPage(index);
        // Important: the destination bitmap must be ARGB (not RGB).
        Bitmap bitmap = Bitmap.createBitmap(currentPage.getWidth(), currentPage.getHeight(), Bitmap.Config.ARGB_8888);

        // Here, we render the page onto the Bitmap.
        // To render a portion of the page, use the second and third parameter. Pass nulls to get the default result.
        // Pass either RENDER_MODE_FOR_DISPLAY or RENDER_MODE_FOR_PRINT for the last parameter.
        currentPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);

        // Display the page
        pageImage.setImage(bitmap);
    }
}

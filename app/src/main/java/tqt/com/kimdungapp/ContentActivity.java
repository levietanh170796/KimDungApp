package tqt.com.kimdungapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import tqt.com.kimdungapp.CheckSyntax.Rule;
import tqt.com.kimdungapp.CheckSyntax.Rules2;
import tqt.com.kimdungapp.database.DatabaseHelperSdcard;
import tqt.com.kimdungapp.entry.Chap;

public class ContentActivity extends AppCompatActivity {

    private final int REQ_CODE_SPEECH_INPUT = 100;

    private TextView txtContent;
    private TextView txtTitle,txtPage;
    private EditText editSearch;
    private ImageView imgBack,imgMic,imgNext,imgPre;
    private ArrayList<Chap> listChap;
    public DatabaseHelperSdcard databaseHelperSdcard;
    private int currPage;
    private ProgressBar progress;
    private Button btnCheck;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        // set cac doi tuong view
        txtContent = (TextView)findViewById(R.id.text_content);
        txtPage = (TextView)findViewById(R.id.text_page);
        txtTitle = (TextView)findViewById(R.id.text_title_name_story);
        imgBack = (ImageView)findViewById(R.id.image_back);
        imgMic = (ImageView)findViewById(R.id.img_mic);
        imgPre = (ImageView)findViewById(R.id.img_pre);
        imgNext = (ImageView)findViewById(R.id.img_next);

        progress = (ProgressBar)findViewById(R.id.progress_bar);
        btnCheck = (Button)findViewById(R.id.btn_check);



        // get dữ liệu từ intent gửi từ main activity
        Intent intent = getIntent();
        txtTitle.setText(intent.getExtras().getString("title"));
        txtContent.setText(Html.fromHtml(intent.getExtras().getString("content")));

        // bắt sự kiến khi click vào image back
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // bắt sự kiên tra cứu bằng giọng nói
        imgMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Nói bây giờ");
                intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);

                String languagePref = "vi";//or, whatever iso code...
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, languagePref);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, languagePref);
                intent.putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE, languagePref);
                //intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,Locale.getDefault());
                startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
            }
        });

        databaseHelperSdcard = new DatabaseHelperSdcard(getApplicationContext());

        // get list chap by stid from tabe st_kim_dung;
        listChap = databaseHelperSdcard.getListChapByStID(intent.getExtras().getInt("stid"));
        currPage = 0;

        imgNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currPage++;
                txtPage.setText(listChap.get(currPage).getDeName());
                txtContent.setText(Html.fromHtml(listChap.get(currPage).getDeContent()));
                String xau =" tôi la thagn c2 dii hco ở truong bách khoa, hnoi têbuốt";



            }
        });

        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 new AsyTaskCheckErrWord(listChap.get(currPage).getDeContent()).execute();
            }
        });
        imgPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currPage>1){
                    currPage--;
                    txtPage.setText(listChap.get(currPage).getDeName());
                    txtContent.setText(Html.fromHtml(listChap.get(currPage).getDeContent()));

                }

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    editSearch.setText(result.get(0));
                }
                break;
            }

        }
    }



    private class AsyTaskCheckErrWord extends AsyncTask<String,String,String> {
        String content;

        public AsyTaskCheckErrWord(String content) {
            this.content = content;
        }

        @Override
        protected String doInBackground(String... params) {

            publishProgress();
            String xau = listChap.get(currPage).getDeContent().replaceAll("<br/>"," <br/> ");
            String[] arrString = xau.split(" ");
            String result = "";
            Rule rule = new Rule();
            Rules2 rules2 = new Rules2();

            for(int i=0; i< arrString.length ; i++){


                //String s = arrString[i].trim().replaceAll("[\\-\\+\\.\\^:,]","");

                String s = arrString[i].trim().replaceAll("[?;!\\(\\)'*\"\\[\\]«»…，。；？！ ]", "");
                s = s.replaceAll("[\\-\\+\\.\\:,]","");




                   if(arrString[i].contains("-")&&arrString[i].length()<2){
                       result = result + arrString[i].trim() + " ";
                   } else {
                       if (rule.checkVowelTotal(s)) {
                           if (rules2.check(s)) {
                               result = result + arrString[i].trim() + " ";
                           } else
                               result = result + "<font color='#E65100'>" + arrString[i] + " " + "</font>";
                       } else
                           result = result + "<font color='#E65100'>" + arrString[i] + " " + "</font>";

                   }
                //publishProgress(result);

            }


            return  result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progress.setVisibility(View.INVISIBLE);
            txtContent.setVisibility(View.VISIBLE);
            txtContent.setText(Html.fromHtml(result));

        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            txtContent.setVisibility(View.INVISIBLE);
            progress.setVisibility(View.VISIBLE);
        }
    }


}

package mysqltest.app.mysqlconntectiontest;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity {
    TextView textView ;
    public static final String DB_URL = "jdbc:mysql://10.0.2.2/erdo";
    public static final String DB_URL1 = "jdbc:mysql://localhost:8080/erdogan?user=erdogan&password=123456&useUnicode=true&characterEncoding=UTF-8";
    private static final String USER = "root";
    private static final String PASS = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
    }
    public void getLast(View view){
        //button assyn task görevi yapcak arkada threadi yormucak uygulama kitlenmicek
        Send objSend = new Send();
        objSend.execute();

    }
    private class Send extends AsyncTask<String,String,String>
    {
        String msg = "";
        String text = textView.getText().toString();
        String text2;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getApplicationContext(),"Lütfen Bekleyin Pre çalışıyor",Toast.LENGTH_LONG).show();
        }

        @Override
        protected String doInBackground(String... strings)
        {
            try{
                //burası bağlantı için
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                java.sql.Connection conn = DriverManager.getConnection(DB_URL,USER,PASS);
                if (conn == null){
                    msg = "Connection goes wrong boş bağlantı hatası";
                }else {
                    String query = "SELECT post_title FROM wp_posts WHERE id=1 ";
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs != null){
                        while(rs.next()){
                            try{
                                //tek sorgu dönüp onuda post date yazcak
                                text2 = rs.getString("post_title");

                                //mesajı göster
                                //Toast.makeText(getApplicationContext(),text2,Toast.LENGTH_LONG).show();
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            msg = text2;
                        }
                    }else {
                        msg = "No Data Found";
                    }

                }

            }catch (Exception e){
                msg = "Connection error hata!!";
                e.printStackTrace();
                Writer writer = new StringWriter();
                e.printStackTrace(new PrintWriter(writer));
                msg = writer.toString();
            }


            return msg;
        }

        @Override
        protected void onPostExecute(String msg) {
            textView.setText(msg);
        }
    }
}

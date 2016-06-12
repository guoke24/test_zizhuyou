package guoke.db;
 
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream; 

import com.example.demo_zizhuyou.R;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;
 
public class DBManager {
    private final int BUFFER_SIZE = 1024;
    public static final String DB_NAME = "threelevelcitys.s3db";
    public static final String PACKAGE_NAME = "com.example.demo_zizhuyou";
    public static final String DB_PATH = "/data"
            + Environment.getDataDirectory().getAbsolutePath() + "/"+ PACKAGE_NAME;
    private SQLiteDatabase database;
    private Context context;
    private File file=null;
    
    public DBManager(Context context) {
    	Log.e("cc", "DBManager");
        this.context = context;
    }
 
    /**
     * 
     */
    public void openDatabase() {
    	Log.e("cc", "openDatabase()");
        this.database = this.openDatabase(DB_PATH + "/" + DB_NAME);
    }
    
    /**
     * 
     * @return
     */
    public SQLiteDatabase getDatabase(){
    	Log.e("cc", "getDatabase()");
    	return this.database;
    }
 
    /**
     * 根据路径来打�?数据库，路径为参数dbfile
     * 如果数据库不存在，就读取city.s3db文件来，创建数据�?
     * @param dbfile 数据库的路径
     * @return
     */
    private SQLiteDatabase openDatabase(String dbfile) {
        try {
        	Log.e("cc", "open and return");
        	file = new File(dbfile);
            if (!file.exists()) {//如果文件不存在，从city.s3db文件
            	Log.e("cc", "file");
            	InputStream is = context.getResources().openRawResource(R.raw.threelevelcitys);
            	if(is!=null){
            		Log.e("cc", "is null");
            	}else{
            	}
            	FileOutputStream fos = new FileOutputStream(dbfile);
            	if(is!=null){
            		Log.e("cc", "fosnull");
            	}else{
            	}
                byte[] buffer = new byte[BUFFER_SIZE];
                int count = 0;
                while ((count =is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                		Log.e("cc", "while");
                	fos.flush();
                }
                fos.close();
                is.close();
            }
            database = SQLiteDatabase.openOrCreateDatabase(dbfile,null);
            return database;
        } catch (FileNotFoundException e) {
            Log.e("cc", "File not found");
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("cc", "IO exception");
            e.printStackTrace();
        } catch (Exception e){
        	Log.e("cc", "exception "+e.toString());
        }
        return null;
    }
    
    /**
     * 
     */
    public void closeDatabase() {
    	Log.e("cc", "closeDatabase()");
    	if(this.database!=null)
    		this.database.close();
    }
}
package com.sinosoft.fhcs.android.database;

/**
 * @CopyRight: SinoSoft.
 * @Description:城市选择 数据库操作
 * @Author: wangshuangshuang.
 * @Create: 2014年8月15日.
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.sinosoft.fhcs.android.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class DB_City {
	private final int BUFFER_SIZE = 1024;
	public static final String DB_NAME = "area.db";
	public static final String PACKAGE_NAME = "com.sinosoft.fhcs.android";
	public static final String DB_PATH = "/data"
			+ Environment.getDataDirectory().getAbsolutePath() + "/"
			+ PACKAGE_NAME;
	private SQLiteDatabase database;
	private Context context;
	private File file = null;
	private FileOutputStream fos = null;
	private InputStream is = null;

	public DB_City(Context context) {
		Log.d("cc", "DBManager");
		this.context = context;
	}

	public void openDatabase() {
		Log.d("cc", "openDatabase()");
		this.database = this.openDatabase(DB_PATH + "/" + DB_NAME);
	}

	public SQLiteDatabase getDatabase() {
		Log.d("cc", "getDatabase()");
		return this.database;
	}

	private SQLiteDatabase openDatabase(String dbfile) {
		try {
			Log.d("cc", "open and return");
			file = new File(dbfile);
			if (!file.exists()) {
				Log.e("cc", "file");
				is = context.getResources().openRawResource(
						R.raw.area);
				if (is != null) {
					Log.e("cc", "is null");
				} else {
				}
				fos = new FileOutputStream(dbfile);
				if (is != null) {
					Log.e("cc", "fosnull");
				} else {
				}
				byte[] buffer = new byte[BUFFER_SIZE];
				int count = 0;
				while ((count = is.read(buffer)) > 0) {
					fos.write(buffer, 0, count);
//					Log.e("cc", "while");
					fos.flush();
				}
				fos.close();
				is.close();
			}
			database = SQLiteDatabase.openOrCreateDatabase(dbfile, null);
			return database;
		} catch (FileNotFoundException e) {
			Log.d("cc", "File not found");
			e.printStackTrace();
		} catch (IOException e) {
			Log.d("cc", "IO exception");
			e.printStackTrace();
		} catch (Exception e) {
			Log.d("cc", "exception " + e.toString());
		}finally{
			if(fos != null){
				try {
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(is != null){
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
		return null;
	}

	public void closeDatabase() {
		Log.d("cc", "closeDatabase()");
		if (this.database != null)
			this.database.close();
	}
}
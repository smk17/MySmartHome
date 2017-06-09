package com.smk17.android.tools;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.smk17.mysmarthome.MyApplication;

/**
 * ���ݿ���ʰ�����
 * 
 * @author ������
 * @version 1.0
 */
public class ToolDatabase extends OrmLiteSqliteOpenHelper {

	private static String databaseName;
	private static int databaseVersion;
	private static List<Class<?>> table = new ArrayList<Class<?>>();
	private static ToolDatabase dbHelper = null;
	
	 /**
	  * ��������ṩPublic���캯����ʵ�������ø÷�����
	  * @param context ������
	  */
	 public ToolDatabase(Context context) {
		 super(context, databaseName, null, databaseVersion);
	 }

	 /**
	  * ʵ��������
	  * @param dbName ���ݿ�����
	  * @param version  ���ݿ�汾
	  * @return
	  */
	public static ToolDatabase gainInstance(String dbName, int version) {
		if (dbHelper == null) {
			databaseName = dbName;
			databaseVersion = version;
			//����ʽ����public���췽��
			dbHelper = OpenHelperManager.getHelper(
					MyApplication.gainContext(), ToolDatabase.class);
		}
		return dbHelper;
	}

	/**
	 * �ͷ����ݿ�����
	 */
	public void releaseAll() {
		if (dbHelper != null) {
			OpenHelperManager.releaseHelper();
			dbHelper = null;
		}
	}

	/**
	 * ����ʵ��
	 * @param cls ʵ��
	 */
	public void addEntity(Class<?> cls) {
		table.add(cls);
	}

	/**
	 * ɾ����
	 * @param entity ʵ��
	 */
	public void dropTable(Class<?> entity) {
		try {
			TableUtils.dropTable(getConnectionSource(), entity, true);
		} catch (SQLException e) {
			Log.e(ToolDatabase.class.getName(), "Unable to drop datbases", e);
		}
	}

	/**
	 * ������
	 * @param entity ʵ��
	 */
	public void createTable(Class<?> entity) {
		try {
			TableUtils.createTableIfNotExists(getConnectionSource(), entity);
		} catch (SQLException e) {
			Log.e(ToolDatabase.class.getName(), "Unable to drop datbases", e);
		}
	}

	/**
	 * ����SQLite���ݿ�
	 */
	@Override
	public void onCreate(SQLiteDatabase sqliteDatabase,
			ConnectionSource connectionSource) {
		try {
			for (Class<?> entity : table) {
				TableUtils.createTableIfNotExists(connectionSource, entity);
			}
		} catch (SQLException e) {
			Log.e(ToolDatabase.class.getName(), "Unable to create datbases", e);
		}
	}

	/**
	 * ����SQLite���ݿ�
	 */
	@Override
	public void onUpgrade(SQLiteDatabase sqliteDatabase,
			ConnectionSource connectionSource, int oldVer, int newVer) {
		try {
			for (Class<?> entity : table) {
				TableUtils.dropTable(connectionSource, entity, true);
			}
			onCreate(sqliteDatabase, connectionSource);
		} catch (SQLException e) {
			Log.e(ToolDatabase.class.getName(),
					"Unable to upgrade database from version " + oldVer
							+ " to new " + newVer, e);
		}
	}
}
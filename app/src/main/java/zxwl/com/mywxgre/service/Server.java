package zxwl.com.mywxgre.service;

import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.webkit.MimeTypeMap;

import java.io.File;

/**
 * Created by Administrator on 2018/1/3.
 */

public class Server extends Service {

    private DownloadManager downloadManager;
    private long mTaskId;

    @Nullable
    @Override
    public void onCreate() {
        Log.i("Tag", "ExampleService-onCreate");
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        Log.i("Tag", "ExampleService-onStart");
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String url = intent.getStringExtra("url");




        downloadAPK(url);

        Log.i("Tag", url);
        //执行文件的下载或者播放等操作
        Log.i("Tag", "ExampleService-onStartCommand");
        /*
         * 这里返回状态有三个值，分别是:
         * 1、START_STICKY：当服务进程在运行时被杀死，系统将会把它置为started状态，但是不保存其传递的Intent对象，之后，系统会尝试重新创建服务;
         * 2、START_NOT_STICKY：当服务进程在运行时被杀死，并且没有新的Intent对象传递过来的话，系统将会把它置为started状态，
         *   但是系统不会重新创建服务，直到startService(Intent intent)方法再次被调用;
         * 3、START_REDELIVER_INTENT：当服务进程在运行时被杀死，它将会在隔一段时间后自动创建，并且最后一个传递的Intent对象将会再次传递过来。
         */
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i("Tag", "ExampleService-onBind");
        return null;
    }

    @Override
    public void onDestroy() {
        Log.i("Tag", "ExampleService-onDestroy");
        super.onDestroy();
    }


    private void downloadAPK(String versionUrl) {
        //创建下载任务
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(versionUrl));
        request.setAllowedOverRoaming(false);//漫游网络是否可以下载

        //设置文件类型，可以在下载结束后自动打开该文件
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        String mimeString = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(versionUrl));
        request.setMimeType(mimeString);

        //在通知栏中显示，默认就是显示的
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setVisibleInDownloadsUi(true);

        //sdcard的目录下的download文件夹，必须设置
        request.setDestinationInExternalPublicDir("/download/", "1702BTK.apk");
        //request.setDestinationInExternalFilesDir(),也可以自己制定下载路径

        //将下载请求加入下载队列
        downloadManager = (DownloadManager) this.getSystemService(Context.DOWNLOAD_SERVICE);
        //加入下载队列后会给该任务返回一个long型的id，
        //通过该id可以取消任务，重启任务等等，看上面源码中框起来的方法
        mTaskId = downloadManager.enqueue(request);

        //注册广播接收者，监听下载状态
        this.registerReceiver(receiver,
                new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            checkDownloadStatus();//检查下载状态
        }
    };
    private void checkDownloadStatus() {
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(mTaskId);//筛选下载任务，传入任务ID，可变参数
        Cursor c = downloadManager.query(query);
        if (c.moveToFirst()) {
            int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
            switch (status) {
                case DownloadManager.STATUS_PAUSED:
                    Log.i("aa",">>>下载暂停");
                case DownloadManager.STATUS_PENDING:
                    Log.i("aa",">>>下载延迟");
                case DownloadManager.STATUS_RUNNING:
                    Log.i("aa",">>>正在下载");
                    break;
                case DownloadManager.STATUS_SUCCESSFUL:
                    Log.i("aa",">>>下载完成");
                    //下载完成安装APK
                    String downloadPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator + "1702BTK.apk";
                    Log.e("AAA",downloadPath);
                    installAPK(new File(downloadPath));
                    break;
                case DownloadManager.STATUS_FAILED:
                    Log.i("aa",">>>下载失败");
                    break;
            }
        }
    }

    protected void installAPK(File file) {
        Log.e("AAB",file.toString());
        if (!file.exists()) return;
        Log.e("AAB",file.toString());

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.parse("file://" + file.toString());
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        startActivity(intent);


    }





}

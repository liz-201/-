package com.lelazy.yuanting.aboutPingzi;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.widget.Toast;

import com.lelazy.yuanting.classes.nettools;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class upload_file_service extends IntentService {

    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.lelazy.yuanting.aboutPingzi.action.FOO";
    private static final String ACTION_BAZ = "com.lelazy.yuanting.aboutPingzi.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.lelazy.yuanting.aboutPingzi.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.lelazy.yuanting.aboutPingzi.extra.PARAM2";

    public upload_file_service() {
        super("upload_file_service");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, upload_file_service.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, upload_file_service.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        /*if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FOO.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionFoo(param1, param2);
            } else if (ACTION_BAZ.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionBaz(param1, param2);
            }
        }*/
        String externalpath=intent.getStringExtra("externalpath");
        String pingziid=intent.getStringExtra("pingziid");

                File f = new File(externalpath + "/" + pingziid);
                for (int i = 0; i < f.listFiles().length; i++) {
                    try {
                        nettools.netmethods.uploadfiles(f.listFiles()[i]);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                Toast.makeText(getApplicationContext(),"shangchuan wanbi",Toast.LENGTH_SHORT).show();
            }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
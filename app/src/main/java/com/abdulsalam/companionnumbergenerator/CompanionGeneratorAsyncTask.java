package com.abdulsalam.companionnumbergenerator;

import android.annotation.SuppressLint;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.Context;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class CompanionGeneratorAsyncTask extends AsyncTask<String, String, Boolean> {

    private WeakReference<Context> contextRef;
    private WeakReference<GenerationFinishedListener> listener;

    public CompanionGeneratorAsyncTask(Context context, GenerationFinishedListener listener) {
        contextRef = new WeakReference<>(context);
        this.listener = new WeakReference<GenerationFinishedListener>(listener);
    }

    @Override
    protected Boolean doInBackground(String... params) {
        storeContacts(generateNumbersArray(params[0]));
        return true;

    }

    @Override
    protected void onPostExecute(Boolean result) {
        if (listener.get() != null) {
            GenerationFinishedListener tmpListener = listener.get();
            tmpListener.didFinish();
        }
    }


    @SuppressLint("DefaultLocale")
    private ArrayList<String> generateNumbersArray(String number) {
        String prefix = number.substring(0, number.length() - 3);
        ArrayList<String> numbers = new ArrayList<>();

        for (int suffix = 0; suffix < 1000; suffix++) {
            numbers.add(prefix + String.format("%03d", suffix));
        }

        return numbers;
    }

    private void storeContacts(ArrayList<String> numbers) {
        for (String number : numbers) {
            ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

            ops.add(ContentProviderOperation.newInsert(
                    ContactsContract.RawContacts.CONTENT_URI)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                    .withValue(ContactsContract.RawContacts.AGGREGATION_MODE, ContactsContract.RawContacts.AGGREGATION_MODE_DISABLED)
                    .build());

            if (number != null) {
                ops.add(ContentProviderOperation.newInsert(
                        ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                        .withValue(ContactsContract.Data.MIMETYPE,
                                ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                        .withValue(
                                ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                                number).build());
            }

            if (number != null) {
                ops.add(ContentProviderOperation.
                        newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                        .withValue(ContactsContract.Data.MIMETYPE,
                                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, number)
                        .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                                ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                        .build());
            }

            Context context = contextRef.get();
            if (context != null) {
                try {
                    ContentProviderResult[] results = context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
                    System.out.println(results.length);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}

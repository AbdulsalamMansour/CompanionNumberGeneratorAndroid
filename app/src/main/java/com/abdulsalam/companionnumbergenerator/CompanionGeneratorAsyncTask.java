package com.abdulsalam.companionnumbergenerator;

import android.annotation.SuppressLint;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.OperationApplicationException;
import android.os.AsyncTask;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class CompanionGeneratorAsyncTask extends AsyncTask<String, String, Exception> {

    private static final int BATCH_SIZE = 498;

    private WeakReference<Context> contextRef;
    private WeakReference<GenerationFinishedListener> listener;

    public CompanionGeneratorAsyncTask(Context context, GenerationFinishedListener listener) {
        contextRef = new WeakReference<>(context);
        this.listener = new WeakReference<>(listener);
    }

    @Override
    protected Exception doInBackground(String... params) {
        return generateAndStoreNumbers(params[0]);
    }

    @Override
    protected void onPostExecute(Exception result) {
        if (listener.get() != null) {
            GenerationFinishedListener tmpListener = listener.get();
            tmpListener.didFinish();
        }

        if(result != null) {
            if(contextRef.get() != null) {
                Toast.makeText(contextRef.get(), R.string.msg_an_error_occurred, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(contextRef.get(), R.string.msg_contacts_added, Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("DefaultLocale")
    private Exception generateAndStoreNumbers(String number) {
        String prefix = number.substring(0, number.length() - 3);
        ArrayList<ContentProviderOperation> ops = new ArrayList<>();

        for (int suffix = 0; suffix < 1000; suffix++) {
            addSingleContact(ops, prefix + String.format("%03d", suffix));
            if (ops.size() >= BATCH_SIZE) {
                Context context = contextRef.get();
                if (context != null) {
                    try {
                        context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
                        ops.clear();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                        return e;
                    } catch (OperationApplicationException e) {
                        e.printStackTrace();
                        return e;
                    }
                }
            }
        }
        return null;
    }

    private void addSingleContact(ArrayList<ContentProviderOperation> ops, String insertedNumber) {
        int rawInsertIndex = ops.size();

        ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .withValue(ContactsContract.RawContacts.AGGREGATION_MODE, ContactsContract.RawContacts.AGGREGATION_MODE_DISABLED)
                .build());

        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawInsertIndex)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, insertedNumber)
                .build());

        ops.add(ContentProviderOperation.
                newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawInsertIndex)
                .withValue(ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, insertedNumber)
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                .build());
    }
}

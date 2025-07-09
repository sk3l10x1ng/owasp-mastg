package org.owasp.mastestapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Base64;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: MastgTest.kt */
@Metadata(d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u000f\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0004\b\u0004\u0010\u0005J\u0006\u0010\u0006\u001a\u00020\u0007J\u000e\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\f"}, d2 = {"Lorg/owasp/mastestapp/MastgTest;", "", "context", "Landroid/content/Context;", "<init>", "(Landroid/content/Context;)V", "mastgTest", "", "processIntent", "", "intent", "Landroid/content/Intent;", "app_debug"}, k = 1, mv = {2, 0, 0}, xi = 48)
/* loaded from: classes3.dex */
public final class MastgTest {
    public static final int $stable = 8;
    private final Context context;

    public MastgTest(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        this.context = context;
    }

    public final String mastgTest() {
        String status;
        BaseUser user = UserManager.INSTANCE.getCurrentUser();
        if ((user instanceof AdminUser) && ((AdminUser) user).getIsAdmin()) {
            status = "PRIVILEGED ADMIN!";
        } else {
            status = "(Not an Admin)";
        }
        String resultString = "Current User: " + user.getUsername() + "\nStatus: " + status + "\n\nVulnerability: Unwanted Object Deserialization is active.\nThe app will deserialize any 'BaseUser' subclass from the 'payload_b64' extra, overwriting the current user state.";
        Log.d("MASTG-TEST", resultString);
        return resultString;
    }

    public final void processIntent(Intent intent) throws ClassNotFoundException, IOException {
        Intrinsics.checkNotNullParameter(intent, "intent");
        if (intent.hasExtra("payload_b64")) {
            String b64Payload = intent.getStringExtra("payload_b64");
            Log.d("VULN_APP", "Received a base64 payload. Deserializing user object...");
            try {
                byte[] serializedPayload = Base64.getDecoder().decode(b64Payload);
                ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(serializedPayload));
                Object untrustedObject = ois.readObject();
                ois.close();
                if (untrustedObject instanceof BaseUser) {
                    UserManager.INSTANCE.setCurrentUser((BaseUser) untrustedObject);
                    Log.i("VULN_APP", "User state overwritten with deserialized object!");
                } else {
                    Log.w("VULN_APP", "Deserialized object was not a user. State unchanged.");
                }
            } catch (Exception e) {
                Log.e("VULN_APP", "Failed to deserialize payload", e);
            }
        }
    }
}

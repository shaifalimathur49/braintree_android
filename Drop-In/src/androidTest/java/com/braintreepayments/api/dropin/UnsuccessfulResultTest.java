package com.braintreepayments.api.dropin;

import android.app.Activity;
import android.content.Intent;
import android.view.KeyEvent;

import com.braintreepayments.api.Braintree;
import com.braintreepayments.api.BraintreeTestUtils;
import com.braintreepayments.api.exceptions.AuthenticationException;
import com.braintreepayments.api.exceptions.AuthorizationException;
import com.braintreepayments.api.exceptions.DownForMaintenanceException;
import com.braintreepayments.api.exceptions.ServerException;
import com.braintreepayments.api.exceptions.UnexpectedException;
import com.braintreepayments.api.exceptions.UpgradeRequiredException;
import com.braintreepayments.testutils.TestClientTokenBuilder;

import java.util.Map;

import static com.braintreepayments.api.BraintreeTestUtils.setUpActivityTest;
import static com.braintreepayments.testutils.ActivityResultHelper.getActivityResult;
import static com.braintreepayments.testutils.ui.Matchers.withId;
import static com.braintreepayments.testutils.ui.ViewHelper.waitForView;
import static com.braintreepayments.testutils.ui.WaitForActivityHelper.waitForActivityToFinish;

public class UnsuccessfulResultTest extends BraintreePaymentActivityTestCase {

    private Braintree mBraintree;
    private BraintreePaymentActivity mActivity;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        String clientToken = new TestClientTokenBuilder().build();
        mBraintree = Braintree.getInstance(mContext, clientToken);
        setUpActivityTest(this, clientToken);
        mActivity = getActivity();

        waitForView(withId(R.id.bt_card_form_header));
    }

    public void testReturnsDeveloperErrorOnAuthenticationException() {
        AuthenticationException exception = new AuthenticationException();
        BraintreeTestUtils.postUnrecoverableErrorFromBraintree(mBraintree, exception);

        waitForActivityToFinish(mActivity);
        Map<String, Object> result = getActivityResult(mActivity);

        assertEquals(BraintreePaymentActivity.BRAINTREE_RESULT_DEVELOPER_ERROR,
                result.get("resultCode"));
        assertEquals(exception, ((Intent) result.get("resultData"))
                .getSerializableExtra(BraintreePaymentActivity.EXTRA_ERROR_MESSAGE));
    }

    public void testReturnsDeveloperErrorOnAuthorizationException() {
        AuthorizationException exception = new AuthorizationException();
        BraintreeTestUtils.postUnrecoverableErrorFromBraintree(mBraintree, exception);

        waitForActivityToFinish(mActivity);
        Map<String, Object> result = getActivityResult(mActivity);

        assertEquals(BraintreePaymentActivity.BRAINTREE_RESULT_DEVELOPER_ERROR,
                result.get("resultCode"));
        assertEquals(exception, ((Intent) result.get("resultData"))
                .getSerializableExtra(BraintreePaymentActivity.EXTRA_ERROR_MESSAGE));
    }

    public void testReturnsDeveloperErrorOnUpgradeRequiredException() {
        UpgradeRequiredException exception = new UpgradeRequiredException();
        BraintreeTestUtils.postUnrecoverableErrorFromBraintree(mBraintree, exception);

        waitForActivityToFinish(mActivity);
        Map<String, Object> result = getActivityResult(mActivity);

        assertEquals(BraintreePaymentActivity.BRAINTREE_RESULT_DEVELOPER_ERROR,
                result.get("resultCode"));
        assertEquals(exception, ((Intent) result.get("resultData"))
                .getSerializableExtra(BraintreePaymentActivity.EXTRA_ERROR_MESSAGE));
    }

    public void testReturnsServerErrorOnServerException() {
        ServerException exception = new ServerException();
        BraintreeTestUtils.postUnrecoverableErrorFromBraintree(mBraintree, exception);

        waitForActivityToFinish(mActivity);
        Map<String, Object> result = getActivityResult(mActivity);

        assertEquals(BraintreePaymentActivity.BRAINTREE_RESULT_SERVER_ERROR,
                result.get("resultCode"));
        assertEquals(exception, ((Intent) result.get("resultData"))
                .getSerializableExtra(BraintreePaymentActivity.EXTRA_ERROR_MESSAGE));
    }

    public void testReturnsServerUnavailableOnDownForMaintenanceException() {
        DownForMaintenanceException exception = new DownForMaintenanceException();
        BraintreeTestUtils.postUnrecoverableErrorFromBraintree(mBraintree, exception);

        waitForActivityToFinish(mActivity);
        Map<String, Object> result = getActivityResult(mActivity);

        assertEquals(BraintreePaymentActivity.BRAINTREE_RESULT_SERVER_UNAVAILABLE,
                result.get("resultCode"));
        assertEquals(exception, ((Intent) result.get("resultData"))
                .getSerializableExtra(BraintreePaymentActivity.EXTRA_ERROR_MESSAGE));
    }

    public void testReturnsServerErrorOnUnexpectedException() {
        UnexpectedException exception = new UnexpectedException();
        BraintreeTestUtils.postUnrecoverableErrorFromBraintree(mBraintree, exception);

        waitForActivityToFinish(mActivity);
        Map<String, Object> result = getActivityResult(mActivity);

        assertEquals(BraintreePaymentActivity.BRAINTREE_RESULT_SERVER_ERROR,
                result.get("resultCode"));
        assertEquals(exception, ((Intent) result.get("resultData"))
                .getSerializableExtra(BraintreePaymentActivity.EXTRA_ERROR_MESSAGE));
    }

    public void testReturnsUserCanceledOnBackButtonPress() {
        sendKeys(KeyEvent.KEYCODE_BACK);

        waitForActivityToFinish(mActivity);
        Map<String, Object> result = getActivityResult(mActivity);

        assertEquals(Activity.RESULT_CANCELED, result.get("resultCode"));
    }

}

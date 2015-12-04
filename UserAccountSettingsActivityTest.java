package com.probisticktechnologies.probistickApp;

import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.action.EspressoKey;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.view.KeyEvent;

import com.probisticktechnologies.probistickApp.account.LoginActivity;
import com.probisticktechnologies.probistickApp.account.RegistrationActivity;
import com.probisticktechnologies.probistickApp.account.UserAccountSettingsActivity;
import com.probisticktechnologies.probistickApp.customMatchers.ErrorTextMatchers;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openContextualActionModeOverflowMenu;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.pressKey;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.isFocusable;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class UserAccountSettingsActivityTest extends ActivityInstrumentationTestCase2<UserAccountSettingsActivity> {

    private UserAccountSettingsActivity mUserAccountSettingsActivity;

    public UserAccountSettingsActivityTest() {
        super(UserAccountSettingsActivity.class);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        mUserAccountSettingsActivity = getActivity();
    }

    /* TESTCASE 1
    Steps:
    First Name edit and change the First Name to a different name Len = 2 characters.
    Click on a different field
    Result:
    First Name entered is displayed.
    Status:
    Implemented
     */
    @Test
    public void testcase1(){
        // Type text and switch the view
        onView(withId(R.id.first_name_editText))
                .perform(typeText("mo"), closeSoftKeyboard());
        onView(withId(R.id.lastName_update_editText)).perform(click());

        // Check that text was changed and no error message is displayed
        onView(withText("mo")).check(matches(isDisplayed()));
    }

    /* TESTCASE 2
    Steps:
    First Name changed to LEN < 2 characters
    Click on a different field
    Result:
    Error 'Pl. enter First Name with minimum 2 characters.'
    Status:
    Implemented
     */
    @Test
    public void testcase2(){
        // Type text and switch the view
        onView(withId(R.id.first_name_editText))
                .perform(typeText("m"), closeSoftKeyboard());
        onView(withId(R.id.lastName_update_editText)).perform(click());

        // Check that error was displayed
        onView(withId(R.id.first_name_editText))
                .check(matches(ErrorTextMatchers
                        .withErrorText(Matchers
                                .containsString(mUserAccountSettingsActivity
                                                .getString(R.string.first_name_min_two_chars_req_error_message)
                                ))));
    }


    /* TESTCASE 3
    Steps:
    First Name Entered = 40 characters (max allowed length is 40 chars)
    Click on a different field
    Result:
    First Name entered is displayed.
    Status:
    Implemented
     */
    @Test
    public void testcase3(){
        // Type text and switch the view
        String firstName = "abcdefghijklmnopqrstuvwxyzabcdefghijklmn";
        onView(withId(R.id.first_name_editText))
                .perform(typeText(firstName), closeSoftKeyboard());
        onView(withId(R.id.lastName_update_editText)).perform(click());

        // Check that error was displayed
        onView(withText(firstName)).check(matches(isDisplayed()));
    }

    /* TESTCASE 4
    Steps:
    User First Name Entered 45 characters (max length check)
    Click on a different field
    Result:
    Error 'First Name can be of length between 2 and 40 characters'
    Status:
    Implemented
     */
    @Test
    public void testcase4(){
        // Type text and switch the view
        String firstName = "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuv";
        onView(withId(R.id.first_name_editText))
                .perform(typeText(firstName), closeSoftKeyboard());
        onView(withId(R.id.lastName_update_editText)).perform(click());

        // Check that error was displayed
        onView(withId(R.id.first_name_editText))
                .check(matches(ErrorTextMatchers
                        .withErrorText(Matchers
                                .containsString(mUserAccountSettingsActivity
                                                .getString(R.string.first_name_max_limit_40_chars_error_message)
                                ))));
    }

    /* TESTCASE 5
    Steps:
    After Changing First Name to Len = 1.
    Click on Menu Option
    Result:
    Error 'Pl. enter First Name with minimum 2 characters.'
    Status:
    Implemented
     */
    @Test
    public void testcase5(){
        // Type text and switch the view
        String firstName = "m";
        onView(withId(R.id.first_name_editText))
                .perform(typeText(firstName), closeSoftKeyboard());
        openContextualActionModeOverflowMenu();

        // Check that error was displayed
        onView(withId(R.id.first_name_editText))
                .check(matches(ErrorTextMatchers
                        .withErrorText(Matchers
                                .containsString(mUserAccountSettingsActivity
                                                .getString(R.string.first_name_min_two_chars_req_error_message)
                                ))));
    }

    /* TESTCASE 6
    Steps:
    After Changing First Name to Len = 1.
    Press 'Back' button on title-bar
    Result:
    The Original Name is restored. The previously opened page (My Account Settings page) is shown.
    Status:
    Not-Implemented
     */
    @Test
    public void testcase6(){
    }

    /* TESTCASE 7
    Steps:
    After Changing First Name to Len = 4.
    Press 'Back' button on title-bar
    Result:
    The Original Name is restored. The previously opened page (My Account Settings page) is shown.
    Status:
    Not-Implemented
     */
    @Test
    public void testcase7(){
    }

    /* TESTCASE 8
    Steps:
    After Changing First Name to Len = 1.
    Press 'Back' button of the PHONE.
    Result:
    The Keyboard is rolled down. The Original First Name remains unchanged in database. When this page(Edit Profile)  is opened again, the Original First Name is shown.
    Status:
    Not-Implemented
     */
    @Test
    public void testcase8(){
    }

    /* TESTCASE 9
    Steps:
    After Changing First Name to Len = 4 (valid length).
    Press 'Back' button  of the PHONE twice.
    Result:
    The Keyboard is rolled down, showing complete page of Edit Profile. On 2nd time 'Back' button click, the 'My Account Settings' Page is shown. The Original First Name remains unchanged in database. When this page(Edit Profile)  is opened again, the Original First Name is shown.
    Status:
    Not-Implemented
     */
    @Test
    public void testcase9(){
    }

    /* TESTCASE 10
    Steps:
    Last Name edit and change the First Name to a different name Len = 1 character.
    Click on a different field
    Result:
    The Changed Last Name is displayed
    Status:
    Implemented
     */
    @Test
    public void testcase10(){
        // Type text and switch the view
        String lastName = "k";
        onView(withId(R.id.lastName_update_editText))
                .perform(typeText(lastName), closeSoftKeyboard());
        onView(withId(R.id.first_name_editText)).perform(click());

        // Check that text was changed and no error message is displayed
        onView(withText(lastName)).check(matches(isDisplayed()));
    }

    /* TESTCASE 11
    Steps:
    Edit and Delete Last Name contents.
    Click on a different field
    Result:
    Last Name = empty.
    Status:
    Implemented
     */
    @Test
    public void testcase11(){
        // Type text and switch the view
        String lastName = "kumar";
        onView(withId(R.id.lastName_update_editText))
                .perform(typeText(lastName), closeSoftKeyboard());
        onView(withId(R.id.first_name_editText)).perform(click());

        // Clear Text
        onView(withId(R.id.lastName_update_editText)).perform(clearText());

        // Check that text was cleared
        onView(allOf(withId(R.id.lastName_update_editText), withText(""))).check(matches(isDisplayed()));
    }

    /* TESTCASE 12
    Steps:
    Last Name Entered = 40 characters (max allowed length is 40 chars)
    Click on a different field
    Result:
    Last Name entered is displayed.
    Status:
    Implemented
     */
    @Test
    public void testcase12(){
        // Type text and switch the view
        String lastName = "abcdefghijklmnopqrstuvwxyzabcdefghijklmn";
        onView(withId(R.id.lastName_update_editText))
                .perform(typeText(lastName), closeSoftKeyboard());
        onView(withId(R.id.first_name_editText)).perform(click());

        // Check that error was displayed
        onView(withText(lastName)).check(matches(isDisplayed()));
    }

    /* TESTCASE 13
    Steps:
    Last Name Entered 45 characters (max length check)
    Click on a different field
    Result:
    Error 'Last Name can be maximum of 40 characters'
    Status:
    Implemented
     */
    @Test
    public void testcase13(){
        // Type text and switch the view
        String lastName = "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuv";
        onView(withId(R.id.lastName_update_editText))
                .perform(typeText(lastName), closeSoftKeyboard());
        onView(withId(R.id.first_name_editText)).perform(click());

        // Check that error was displayed
        onView(withId(R.id.lastName_update_editText))
                .check(matches(ErrorTextMatchers
                        .withErrorText(Matchers
                                .containsString(mUserAccountSettingsActivity
                                                .getString(R.string.first_name_max_limit_40_chars_error_message)
                                ))));
    }

    /* TESTCASE 14
    Steps:
    Email-ID: Field is displayed in a disabled manner. Try editing this field. Keyboard is not shown. No editing allowed.
    Click on Email-id field
    Result:
    Nothing happens.
    Status:
    Implemented
     */
    @Test
    public void testcase14(){
        // Check whether field is enabled or disabled
        onView(withId(R.id.email_id_registration_editText)).check(matches(not(isEnabled())));
    }

    /* TESTCASE 15
    Steps:
    Password: It shows multiple stars ('*').  Field is displayed in a disabled manner. Try editing this field. Keyboard is not shown. No editing allowed.
    Click on Password field
    Result:
    Nothing happens.
    Status:
    Implemented
     */
    @Test
    public void testcase15(){
        // Check whether field is enabled or disabled
        onView(withId(R.id.password_editText)).check(matches(not(isEnabled())));
    }

    /* TESTCASE 16
    Steps:
    Mobile Number: Number Change accepted: Edit Number to Len = 10 digits (without leading zeros).
    Click on a different field
    Result:
    Numeric Keyboard appears. The valid changes are accepted. A warning message appears 'The New Mobile number will have to be verified before it is accepted. Otherwise, old number will be reverted to.'
    Status:
    Not-Implemented
     */
    @Test
    public void testcase16(){
    }

    /* TESTCASE 17
    Steps:
    Mobile Number: Special Characters disallowed: Edit Number to Len = 10 digits (without leading zeros). Special characters = (e.g.,  #, +, *, space etc.)
    Click on a different field
    Result:
    Numeric Keyboard appears. The changed number is not accepted. Error 'Mobile number can only consist of 10 digits, and no special characters are allowed either.' . Original Number is displayed back.
    Status:
    Implemented
     */
    @Test
    public void testcase17(){
        // Type text and switch the view
        String mobileNo = "^^&&**##@@";
        onView(withId(R.id.mobile_no_editText))
                .perform(typeText(mobileNo), closeSoftKeyboard());
        onView(withId(R.id.first_name_editText)).perform(click());

        // Check that error was displayed
        onView(withId(R.id.mobile_no_editText))
                .check(matches(ErrorTextMatchers
                        .withErrorText(Matchers
                                .containsString(mUserAccountSettingsActivity
                                                .getString(R.string.mobile_number_no_special_characters_allowed_error_message)
                                ))));

    }

    /* TESTCASE 18
    Steps:
    Mobile Number: Special Characters disallowed and invalid Length: Edit Number to Len < 10 digits (without leading zeros).  Special characters = (e.g.,  #, +, *, space etc.)
    Click on a different field
    Result:
    Numeric Keyboard appears. The changed number is not accepted. Error 'Mobile number can only consist of 10 digits, and no special characters are allowed either.' . Original Number is displayed back.
    Status:
    Implemented
     */
    @Test
    public void testcase18(){
        // Type text and switch the view
        String mobileNo = "9888**##6";
        onView(withId(R.id.mobile_no_editText))
                .perform(typeText(mobileNo), closeSoftKeyboard());
        onView(withId(R.id.first_name_editText)).perform(click());

        // Check that error was displayed
        onView(withId(R.id.mobile_no_editText))
                .check(matches(ErrorTextMatchers
                        .withErrorText(Matchers
                                .containsString(mUserAccountSettingsActivity
                                                .getString(R.string.mobile_number_no_special_characters_allowed_error_message)
                                ))));

    }

    /* TESTCASE 19
    Steps:
    Mobile Number: Invalid Length: Edit Number to Len > 10 digits (without leading zeros).
    Click on a different field
    Result:
    Numeric Keyboard appears. The changed number is not accepted.  Error 'Mobile number can only consist of 10 digits. No special characters are allowed.' . Original Number is displayed back.
    Status:
    Implemented
     */
    @Test
    public void testcase19(){
        // Type text and switch the view
        String mobileNo = "988844116";
        onView(withId(R.id.mobile_no_editText))
                .perform(typeText(mobileNo), closeSoftKeyboard());
        onView(withId(R.id.first_name_editText)).perform(click());

        // Check that error was displayed
        onView(withId(R.id.mobile_no_editText))
                .check(matches(ErrorTextMatchers
                        .withErrorText(Matchers
                                .containsString(mUserAccountSettingsActivity
                                                .getString(R.string.mobile_number_no_special_characters_allowed_error_message)
                                ))));

    }

    /* TESTCASE 20
    Steps:
    Mobile Number: Special Characters and Valid Length: Edit Number to Len = 10 digit and special characters (e.g.,  #, +, *, space etc.)
    Click on a different field
    Result:
    Numeric Keyboard appears. Error 'Mobile number can only consist of 10 digits. No special characters are allowed.' . Original Number is displayed back.The changes number is not accepted.
    Status:
    Implemented
     */
    @Test
    public void testcase20(){
        // Type text and switch the view
        String mobileNo = "9888##%%11";
        onView(withId(R.id.mobile_no_editText))
                .perform(typeText(mobileNo), closeSoftKeyboard());
        onView(withId(R.id.first_name_editText)).perform(click());

        // Check that error was displayed
        onView(withId(R.id.mobile_no_editText))
                .check(matches(ErrorTextMatchers
                        .withErrorText(Matchers
                                .containsString(mUserAccountSettingsActivity
                                                .getString(R.string.mobile_number_no_special_characters_allowed_error_message)
                                ))));

    }

    /* TESTCASE 21
    Steps:
    Mobile Number: Edit Number and Verify it: Edit number to a valid length and characters.
    (1) Click on 'Save' button of the page.(2) After clicking 'Save', the OTP Verification Page appears. The User receives the generated OTP number.
    Result:
    (1) Numeric Keyboard appears. The valid changes are accepted. 'Save' performs saving action for the information. (2) The OTP is entered and verified. Otherwise, the old mobile number is restored.
    Status:
    Not-Implemented
     */
    @Test
    public void testcase21(){
    }

    /* TESTCASE 22
    Steps:
    Mobile Number: Invalid Number Editing: Edit number to a valid length and characters.
    Click on a different field
    Result:
    Numeric Keyboard appears. Some numbers are changed. The user clicks on 'Next' button keyboard. The control moves to next field (DoB). The changed number is lost and original number is restored in the Mobile Number field. No alpha numeic data can be entered into the DoB field, if user clicks 'Next' on numeric keyboard (as above).
    Status:
    Implemented
     */
    @Test
    public void testcase22(){
        // Type text and switch the view
        String mobileNo = "9888446611";
        EspressoKey.Builder keyBuilder = new EspressoKey.Builder();
        onView(withId(R.id.mobile_no_editText))
                .perform(typeText(mobileNo));
        onView(withId(R.id.dateOfBirth_editText)).perform(click());

        // Check whether control moves to dob or not
        onView(withId(R.id.dateOfBirth_editText)).check(matches(not(isEnabled())));
    }

    /* TESTCASE 23
    Steps:
    Date of Birth: Edit Date of Birth to a different dd, mmm and yyyy through the DoB field
    Click on a different field
    Result:
    (1) The Calendar pop-up shows up. The date in the calendar pop-up is the same as the Original-DoB, and NOT any other/current date.(2) User changes the DoB through the pop-up, clicks 'Done' button. The selected date is shown in the DoB field. (3) It is saved, when user Saves it.
    Status:
    Not-Implemented
     */
    @Test
    public void testcase23(){
    }

    /* TESTCASE 24
    Steps:
    Date of Birth: Changes Abandoned by Back Button: Edit Date of Birth to a different dd, mmm and yyyy through the Calendar pop-up control.
    Click on a different field
    Result:
    (1) The Calendar pop-up shows up. The date in the calendar pop-up is the same as the Original-DoB.(2) User selects a new DoB through the pop-up. User clicks 'Back' button of the PHONE. (3) No Changes are accepted in the field. The DoB field keeps the original date.
    Status:
    Not-Implemented
     */
    @Test
    public void testcase24(){
    }

    /* TESTCASE 25
    Steps:
    Date of Birth:  Changes Abandoned by Outside click: Edit Date of Birth to a different dd, mmm and yyyy through the Calendar pop-up control.
    Click on a different field
    Result:
    (1) The Calendar pop-up shows up. The date in the calendar pop-up is the same as the Original-DoB.(2) User selects the new DoB through the pop-up. User does NOT click 'Done' button, but clicks anywhere outside of the Calendar pop-up . (3) No Changes are accepted in the field. The DoB field keeps the original date.
    Status:
    Not-Implemented
     */
    @Test
    public void testcase25(){
    }

}

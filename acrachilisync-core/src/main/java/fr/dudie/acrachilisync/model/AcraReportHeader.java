/*
 * Copyright (C) 2011 Jeremie Huchet
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package fr.dudie.acrachilisync.model;

public enum AcraReportHeader {

    /**
     * REPORT_ID
     * <p>
     * Since: 4.0 - Default: Yes
     * <p>
     * A unique identifier for this report. If you receive 2 reports with the same ID then there
     * might be a bug in the handling of reports in ACRA...
     */
    REPORT_ID,

    /**
     * APP_VERSION_CODE
     * <p>
     * Since: 1.0 - Default: Yes
     * <p>
     * Application version code. This is the incremental integer version code used to differentiate
     * versions on the android market. See PackageInfo.versionCode.
     */
    APP_VERSION_CODE,

    /**
     * APP_VERSION_NAME
     * <p>
     * Since: 1.0 - Default: Yes
     * <p>
     * Application version name. See PackageInfo.versionName.
     */
    APP_VERSION_NAME,

    /**
     * PACKAGE_NAME
     * <p>
     * Since: 1.0 - Default: Yes
     * <p>
     * Application package name. See Context.getPackageName().
     */
    PACKAGE_NAME,

    /**
     * FILE_PATH
     * <p>
     * Since: 1.0 - Default: Yes
     * <p>
     * Base path of the application's private file folder. See
     * http://developer.android.com/reference/android/content/Context.html#getFilesDir()
     * Context.getFilesDir()].
     */
    FILE_PATH,

    /**
     * PHONE_MODEL
     * <p>
     * Since: 1.0 - Default: Yes
     * <p>
     * Device model name. See Build.MODEL.
     */
    PHONE_MODEL,

    /**
     * BRAND
     * <p>
     * Since: 1.0 - Default: Yes
     * <p>
     * Device brand (manufacturer or carrier). See Build.BRAND.
     */
    BRAND,

    /**
     * PRODUCT
     * <p>
     * Since: 1.0 - Default: Yes
     * <p>
     * Device overall product code. See Build.PRODUCT.
     */
    PRODUCT,

    /**
     * ANDROID_VERSION
     * <p>
     * Since: 1.0 - Default: Yes
     * <p>
     * Device android version name. See Build.VERSION.RELEASE
     */
    ANDROID_VERSION,

    /**
     * BUILD
     * <p>
     * Since: 4.0 - Default: Yes
     * <p>
     * Android build details. Here is an example for the NexusOne with official Android 2.3.3:
     * 
     * <pre>
     *    BOARD=mahimahi
     *    BOOTLOADER=0.35.0017
     *    BRAND=google
     *    CPU_ABI=armeabi-v7a
     *    CPU_ABI2=armeabi
     *    DEVICE=passion
     *    DISPLAY=GRI40
     *    FINGERPRINT=google/passion/passion:2.3.3/GRI40/102588:user/release-keys
     *    HARDWARE=mahimahi
     *    HOST=android-test-1.mtv.corp.google.com
     *    ID=GRI40
     *    MANUFACTURER=HTC
     *    MODEL=Nexus One
     *    PRODUCT=passion
     *    RADIO=unknown
     *    SERIAL=HT019P801851
     *    TAGS=release-keys
     *    TIME=1297306326000
     *    TYPE=user
     *    UNKNOWN=unknown
     *    USER=android-build
     * </pre>
     * 
     * See Build.
     */
    BUILD,

    /**
     * TOTAL_MEM_SIZE
     * <p>
     * Since: 1.0 - Default: Yes
     * <p>
     * Estimation of the total device memory size based on filesystem stats.
     */
    TOTAL_MEM_SIZE,

    /**
     * AVAILABLE_MEM_SIZE
     * <p>
     * Since: 1.0 - Default: Yes
     * <p>
     * Estimation of the available device memory size based on filesystem stats.
     */
    AVAILABLE_MEM_SIZE,

    /**
     * CUSTOM_DATA
     * <p>
     * Since: 1.0 - Default: Yes
     * <p>
     * Contains key = value pairs defined by the application developer during the application
     * execution.
     */
    CUSTOM_DATA,

    /**
     * STACK_TRACE
     * <p>
     * Since: 1.0 - Default: Yes
     * <p>
     * The Holy Stack Trace. Details of the exception that caused the application to crash.
     */
    STACK_TRACE,

    /** The MD5 hash for the {@link #stacktrace}. */
    STACK_TRACE_MD5,

    /**
     * INITIAL_CONFIGURATION
     * <p>
     * Since: 3.0 - Default: Yes
     * <p>
     * Configuration fields state on the application start. Example:
     * 
     * <pre>
     *    locale=fr_FR
     *    hardKeyboardHidden=HARDKEYBOARDHIDDEN_YES
     *    keyboard=KEYBOARD_NOKEYS
     *    keyboardHidden=KEYBOARDHIDDEN_NO
     *    fontScale=1.0
     *    mcc=208
     *    mnc=10
     *    navigation=NAVIGATION_TRACKBALL
     *    navigationHidden=NAVIGATIONHIDDEN_NO
     *    orientation=ORIENTATION_PORTRAIT
     *    screenLayout=SCREENLAYOUT_SIZE_NORMAL+SCREENLAYOUT_LONG_YES
     *    seq=117
     *    touchscreen=TOUCHSCREEN_FINGER
     *    uiMode=UI_MODE_TYPE_NORMAL+UI_MODE_NIGHT_NO
     *    userSetLocale=false
     * </pre>
     */
    INITIAL_CONFIGURATION,

    /**
     * CRASH_CONFIGURATION
     * <p>
     * Since: 3.0 - Default: Yes
     * <p>
     * Configuration fields state on the application crash. Example :
     * 
     * <pre>
     *    locale=fr_FR
     *    hardKeyboardHidden=HARDKEYBOARDHIDDEN_YES
     *    keyboard=KEYBOARD_NOKEYS
     *    keyboardHidden=KEYBOARDHIDDEN_NO
     *    fontScale=1.0
     *    mcc=208
     *    mnc=10
     *    navigation=NAVIGATION_TRACKBALL
     *    navigationHidden=NAVIGATIONHIDDEN_NO
     *    orientation=ORIENTATION_LANDSCAPE
     *    screenLayout=SCREENLAYOUT_SIZE_NORMAL+SCREENLAYOUT_LONG_YES
     *    seq=120
     *    touchscreen=TOUCHSCREEN_FINGER
     *    uiMode=UI_MODE_TYPE_NORMAL+UI_MODE_NIGHT_NO
     *    userSetLocale=false
     * </pre>
     * 
     * As an example of how this field combined with initial_configuration can help you debug your
     * application, you can see here that the "seq" field has been increased from 117 to120. This
     * means that 3 configuration changes occurred between the Application start and the crash. You
     * can also see that the orientation changed from PORTRAIT to LANDSCAPE. There could be lots of
     * issues regarding orientation (and other configuration) changes handling, so if you don't
     * understand how a bug happens, having a look at this values can help you understand what the
     * user did with his device. Though, having seq value increased does not mean that these changes
     * occured while your application was active. There can be lots of things happening between your
     * application start and the crash. For example, a user can start your application and use it on
     * day 1 without any issue, use many other applications in between, reopen your application on
     * day 2 (which was still started but paused) and then the crash occurs. All the configuration
     * changes that happened in between introduce a change in the seq value.
     */
    CRASH_CONFIGURATION,

    /**
     * DISPLAY
     * <p>
     * Since: 3.0 - Default: Yes
     * <p>
     * Device display specifications. See WindowManager.getDefaultDisplay() Example:
     * 
     * <pre>
     *    width=480
     *    height=800
     *    pixelFormat=1
     *    refreshRate=60.0fps
     *    metrics.density=x1.5
     *    metrics.scaledDensity=x1.5
     *    metrics.widthPixels=480
     *    metrics.heightPixels=800
     *    metrics.xdpi=254.0
     *    metrics.ydpi=254.0
     * </pre>
     * 
     * /** USER_COMMENT
     * <p>
     * Since: 3.0 - Default: Yes
     * <p>
     * Comment added by the user in the dialog displayed in NOTIFICATION mode.
     */
    DISPLAY,

    /**
     * USER_APP_START_DATE
     * <p>
     * Since: 4.0 - Default: Yes
     * <p>
     * User date on application start.
     */
    USER_APP_START_DATE,

    /**
     * USER_CRASH_DATE
     * <p>
     * Since: 3.0 - Default: Yes
     * <p>
     * User date immediately after the crash occurred.
     */
    USER_CRASH_DATE,

    /**
     * DUMPSYS_MEMINFO
     * <p>
     * Since: 4.0 - Default: Yes (?)
     * <p>
     * Memory state details for your application process. Example (taken from android 2.3.3, content
     * may vary with android versions):
     * 
     * <pre>
     *    Applications Memory Usage (kB):
     *    Uptime: 75158991 Realtime: 192859224
     * 
     *    ** MEMINFO in pid 18806 [org.acra.sampleapp] **
     *                        native   dalvik    other    total
     *                size:     4216     5447      N/A     9663
     *           allocated:     4208     2818      N/A     7026
     *                free:        7     2629      N/A     2636
     *               (Pss):      759      211     2118     3088
     *      (shared dirty):     2296     1860     5052     9208
     *        (priv dirty):      684       64     1436     2184
     *     
     *     Objects
     *               Views:        0        ViewRoots:        0
     *         AppContexts:        0       Activities:        0
     *              Assets:        2    AssetManagers:        2
     *       Local Binders:        7    Proxy Binders:       14
     *    Death Recipients:        1
     *     OpenSSL Sockets:        0
     *     
     *     SQL
     *                   heap:        0         MEMORY_USED:        0
     *     PAGECACHE_OVERFLOW:        0         MALLOC_SIZE:        0
     *     
     *     
     *     Asset Allocations
     *        zip:/data/app/org.acra.sampleapp-1.apk:/resources.arsc: 5K
     * </pre>
     * 
     * Analysis of these data require a high knowledge of internal system memory handling. You can
     * get some clues in this StackOverflow answer from Dianne Hackborn.
     */
    DUMPSYS_MEMINFO,

    /**
     * DROPBOX
     * <p>
     * Since: 4.0 - Default: No
     * <p>
     * Requires READ_LOGS permission. Content of the android.os.DropBoxManager (introduced in API
     * level 8). The DropBoxManager is an alternative logging system which allows platform
     * developers to persist data (text ot binary). There is small chance that these logging events
     * are really useful for debugging your app... but you can add your own tags to be retrieved by
     * ACRA. So if you ever can find some usage for it (maybe related with StrictMode?) then ACRA is
     * already ready to collect them. A few details were given about the usage of the DropBox by
     * Brad Fitzpatrick in this StackOverflow answer. If properly configured to retrieve system
     * tags, here is the kind of content you can retrieve after a fresh boot on a Nexus One (with
     * android 2.3.3):
     * 
     * <pre>
     *    Tag: system_app_anr
     *    Nothing.
     *    Tag: system_app_wtf
     *    Nothing.
     *    Tag: system_app_crash
     *    Nothing.
     *    Tag: system_server_anr
     *    Nothing.
     *    Tag: system_server_wtf
     *    Nothing.
     *    Tag: system_server_crash
     *    Nothing.
     *    Tag: BATTERY_DISCHARGE_INFO
     *    Nothing.
     *    Tag: SYSTEM_RECOVERY_LOG
     *    Nothing.
     *    Tag: SYSTEM_BOOT
     *    @20110327T163719
     *    Text: Build: google/passion/passion:2.3.3/GRI40/102588:user/release-keys
     *    Hardware: mahimahi
     *    Bootloader: 0.35.0017
     *    Radio: unknown
     *    Kernel: Linux version 2.6.35.7-59423-g08607d4 (android-build@apa28.mtv.corp.google.com) (gcc version 4.4.3 (GCC) ) #1 PREEMPT Tue Dec 28 09:34:38 PST 2010
     * 
     * 
     *    Tag: SYSTEM_LAST_KMSG
     *    @20110327T163720
     *    Text: Build: google/passion/passion:2.3.3/GRI40/102588:user/release-keys
     *    Hardware: mahimahi
     *    Bootloader: 0.35.0017
     *    Radio: unknown
     *    Kernel: Linux version 2.6.35.7-59423-g08607d4 (android-build@apa28.mtv.corp.google.com) (gcc version 4.4.3 (GCC) ) #1 PREEMPT Tue Dec 28 09:34:38 PST 2010
     * 
     *    [[TRUNCATED]]
     *    nd
     *    [75771.250976] wake lock alarm_rtc, expired
     *    [75771.256195] suspend: enter suspend
     *    [75771.256378] PM: Syncing filesystems ... done.
     *    [75771.257965] Freezing user space processes ... (elapsed 0.02 seconds) d
     *    Tag: APANIC_CONSOLE
     *    Nothing.
     *    Tag: APANIC_THREADS
     *    Nothing.
     *    Tag: SYSTEM_RESTART
     *    Nothing.
     *    Tag: SYSTEM_TOMBSTONE
     *    Nothing.
     *    Tag: data_app_strictmode
     *    Nothing.
     * </pre>
     */
    DROPBOX,

    /**
     * LOGCAT
     * <p>
     * Since: 4.0 - Default: Yes
     * <p>
     * Requires READ_LOGS permission. Logcat default extract. The logcat is the Android Developer's
     * best friend. This is where all applications and the system write their debug traces.
     * Retrieving logcat events in your reports will allow you to retrieve your own debugging
     * traces. See configuration details.
     */
    LOGCAT,

    /**
     * EVENTSLOG
     * <p>
     * Since: 4.0 - Default: No
     * <p>
     * Requires READ_LOGS permission. Logcat eventslog extract. The eventslog is a lower level
     * logging buffer. Its content looks like this:
     * 
     * <pre>
     *    03-27 16:37:45.230 I/content_update_sample(  428): [content://gmail-downloads/download,delete,_id IN (),2328,,100]
     *    03-27 16:37:45.550 I/dvm_gc_info(  203): [7017575181485061379,-9057997136484935626,-3939377503288981465,9365717]
     *    03-27 16:37:45.560 I/am_create_task(  103): 4
     *    03-27 16:37:45.560 I/am_create_activity(  103): [1083121280,4,org.acra.sampleapp/.CrashTestLauncher,android.intent.action.MAIN,NULL,NULL,270532608]
     *    03-27 16:37:45.570 I/am_pause_activity(  103): [1079760528,org.adwfreak.launcher/.Launcher]
     *    03-27 16:37:45.760 I/am_on_paused_called(  203): org.adwfreak.launcher.Launcher
     *    03-27 16:37:45.770 I/am_proc_start(  103): [559,10059,org.acra.sampleapp,activity,org.acra.sampleapp/.CrashTestLauncher]
     *    03-27 16:37:45.810 I/dvm_gc_madvise_info(  559): [1290240,1056768]
     *    03-27 16:37:45.850 I/am_proc_bound(  103): [559,org.acra.sampleapp]
     *    03-27 16:37:45.850 I/am_restart_activity(  103): [1083121280,4,org.acra.sampleapp/.CrashTestLauncher]
     *    03-27 16:37:45.980 I/db_sample(  428): [/data/data/com.google.android.gm/databases/XX@YY,COMMIT;DELETE FROM attachments WHERE _id IN (),695,,100]
     *    03-27 16:37:46.080 I/db_sample(  428): [/data/data/com.google.android.gm/databases/XX@YY,GETLOCK:SELECT * FROM custom_label_color_prefs,8122,,100]
     *    03-27 16:37:46.150 I/binder_sample(  176): [android.view.IWindowSession,4,7,com.joko.lightgridpro,1]
     *    03-27 16:37:46.410 I/db_sample(  428): [/data/data/com.google.android.gm/databases/XX@YY,SELECT * FROM custom_label_color_prefs,8453,,100]
     *    03-27 16:37:46.410 I/db_sample(  428): [/data/data/com.google.android.gm/databases/downloads.db,SELECT _id FROM downloads WHERE (status >= '200') ORDER BY lastm,1134,,100]
     *    03-27 16:37:46.410 I/content_query_sample(  428): [content://gmail-downloads/download,_id,status >= '200',lastmod,2036,,100]
     *    03-27 16:37:46.450 I/db_sample(  428): [/data/data/com.google.android.gm/databases/XX@YY,BEGIN EXCLUSIVE;,42,,9]
     *    03-27 16:37:46.570 I/notification_cancel(  103): [com.handcent.nextsms,321,0]
     *    03-27 16:37:46.580 I/am_destroy_service(  103): [1084025168,com.handcent.nextsms/com.handcent.sms.transaction.SmsReceiverService,540]
     *    03-27 16:37:46.590 I/am_kill (  103): [300,com.android.settings,14,too many background]
     *    03-27 16:37:46.590 I/am_proc_died(  103): [300,com.android.settings]
     *    03-27 16:37:46.600 I/db_sample(  428): [/data/data/com.google.android.gm/databases/XX@YY,COMMIT;SELECT _id FROM attachments WHERE downloadId == 0,102,,21]
     *    03-27 16:37:46.620 I/gmail_perf_end(  428): [ME.constructor,132,11014,1]
     *    03-27 16:37:46.630 I/content_query_sample(  411): [content://gmail-ls/labels/XX@YY,canonicalName/numUnreadConversations,,,17123,,100]
     *    03-27 16:37:46.660 I/am_on_resume_called(  559): org.acra.sampleapp.CrashTestLauncher
     *    03-27 16:37:46.720 I/activity_launch_time(  103): [1083121280,org.acra.sampleapp/.CrashTestLauncher,949,29542]
     *    03-27 16:37:46.840 I/am_destroy_service(  103): [1083808248,com.google.android.gm/.downloadprovider.DownloadService,428]
     *    03-27 16:37:46.900 I/db_sample(  428): [/data/data/com.google.android.gm/databases/XX@YY,BEGIN EXCLUSIVE;,268,,54]
     *    03-27 16:37:46.970 I/am_create_service(  103): [1084806080,com.nitrodesk.droid20.nitroid/com.nitrodesk.daemon.ExchangeListenerSvc,,275]
     *    03-27 16:37:46.990 I/am_proc_start(  103): [572,10049,com.levelup.beautifulwidgets,broadcast,com.levelup.beautifulwidgets/.HomeWidget14]
     *    03-27 16:37:47.030 I/dvm_gc_madvise_info(  572): [1290240,1056768]
     *    03-27 16:37:47.070 I/am_proc_bound(  103): [572,com.levelup.beautifulwidgets]
     *    03-27 16:37:47.160 I/dvm_gc_info(  103): [8320808730291807962,-8924856104238569357,-3969495325797021657,8620078]
     *    03-27 16:37:47.180 I/db_sample(  428): [/data/data/com.google.android.gm/databases/XX@YY,SELECT IFNULL((SELECT _id FROM conversations WHERE syncRationale,266,,54]
     *    03-27 16:37:47.290 I/db_sample(  275): [/data/data/com.nitrodesk.droid20.nitroid/databases/windroid.db,BEGIN EXCLUSIVE;,149,com.nitrodesk.droid20.nitroid,30]
     *    03-27 16:37:47.300 I/am_kill (  103): [308,com.google.android.partnersetup,14,too many background]
     *    03-27 16:37:47.300 I/am_proc_died(  103): [308,com.google.android.partnersetup]
     *    03-27 16:37:47.550 I/dvm_gc_info(  559): [8314046716718409527,-8925986472288581587,-4000739048211904473,8525718]
     *    03-27 16:37:47.650 I/dvm_gc_info(  559): [8314046716718413429,-8925704997311875024,-3999894623281772505,8525718]
     *    03-27 16:37:47.720 I/db_sample(  275): [/data/data/com.nitrodesk.droid20.nitroid/databases/windroid.db,COMMIT;SELECT _id, AccountID, DeviceID, ServerName, UserID, Doma,111,com.nitrodesk.droid20.nitroid,23]
     *    03-27 16:37:47.980 I/db_sample(  275): [/data/data/com.nitrodesk.droid20.nitroid/databases/windroid.db,BEGIN EXCLUSIVE;,249,com.nitrodesk.droid20.nitroid,50]
     *    03-27 16:37:48.100 I/dvm_gc_info(  559): [8314046716718417551,-9042798587623495631,-3999331673328351193,8525718]
     * </pre>
     */
    EVENTSLOG,

    /**
     * RADIOLOG
     * <p>
     * Since: 4.0 - Default: No
     * <p>
     * Requires READ_LOGS permission. Logcat radio extract. The third buffer managed by logcat
     * contains radio events. It looks like this:
     * 
     * <pre>
     *    03-27 16:37:28.960 D/GSM     (  195): [DSAC DEB] trySetupData with mIsPsRestricted=false
     *    03-27 16:37:28.960 I/GSM     (  195): Preferred APN:20810:20810:SFR, 8, 20810, sl2sfr, , null, , , , -1, default, supl
     *    03-27 16:37:28.960 I/GSM     (  195): Waiting APN set to preferred APN
     *    03-27 16:37:28.960 D/GSM     (  195): [GsmDataConnectionTracker] Create from allApns : [SFR, 8, 20810, sl2sfr, , null, , , , -1, default, supl][SFR-MMS, 9, 20810, mmssfr, , http://mms1, 10.151.0.1, 8080, , -1, mms]
     *    03-27 16:37:28.960 D/GSM     (  195): [GsmDataConnection-1] DcInactiveState msg.what=EVENT_CONNECT
     *    03-27 16:37:28.960 D/GSM     (  195): [GsmDataConnection-1] Connecting to carrier: 'SFR' APN: 'sl2sfr' proxy: '' port: '
     *    03-27 16:37:28.960 D/RILJ    (  195): [0094]> SETUP_DATA_CALL 1 0 sl2sfr   3
     *    03-27 16:37:29.020 D/RILJ    (  195): [0095]> REQUEST_GET_NEIGHBORING_CELL_IDS
     *    03-27 16:37:29.260 D/RILJ    (  195): [0095]< REQUEST_GET_NEIGHBORING_CELL_IDS  
     *    03-27 16:37:32.760 D/RILJ    (  195): [UNSL]< UNSOL_DATA_CALL_LIST_CHANGED [DataCallState: { cid: 1, active: 2, type: IP, apn: sl2sfr, address: 10.114.107.85 }, DataCallState: { cid: -1, active: 0, type: , apn: , address:  }, DataCallState: { cid: -1, active: 0, type: , apn: , address:  }]
     *    03-27 16:37:32.770 D/RILJ    (  195): [0094]< SETUP_DATA_CALL {1, rmnet0, 10.114.107.XX}
     *    03-27 16:37:32.770 D/GSM     (  195): [GsmDataConnection-1] DcActivatingState msg.what=EVENT_SETUP_DATA_CONNECTION_DONE
     *    03-27 16:37:32.770 D/GSM     (  195): [GsmDataConnection-1] interface=rmnet0 ipAddress=10.114.107.XX gateway=10.114.107.86 DNS1=172.20.2.39 DNS2=172.20.2.10
     *    03-27 16:37:32.770 D/GSM     (  195): [GsmDataConnection-1] DataConnection setup result='SUCCESS' on cid=1
     *    03-27 16:37:32.770 D/GSM     (  195): [GsmDataConnection-1] DcInactiveState: setEnterNoticationParams cp,cause
     *    03-27 16:37:32.770 D/GSM     (  195): [GsmDataConnection-1] DcActiveState: enter notifyConnectCompleted
     *    03-27 16:37:32.770 D/GSM     (  195): [GsmDataConnection-1] notifyConnection at 1301236652781 cause=No Error
     *    03-27 16:37:32.790 D/GSM     (  195): [DataConnection] Start poll NetStat
     *    03-27 16:37:33.730 D/RILJ    (  195): [UNSL]< UNSOL_RESPONSE_NETWORK_STATE_CHANGED
     *    03-27 16:37:33.730 D/RILJ    (  195): [0096]> OPERATOR
     *    03-27 16:37:33.730 D/RILJ    (  195): [0097]> GPRS_REGISTRATION_STATE
     *    03-27 16:37:33.730 D/RILJ    (  195): [0098]> REGISTRATION_STATE
     *    03-27 16:37:33.730 D/RILJ    (  195): [0099]> QUERY_NETWORK_SELECTION_MODE
     *    03-27 16:37:33.740 D/RILJ    (  195): [0096]< OPERATOR {F SFR, (N/A), 20810}
     *    03-27 16:37:33.760 D/RILJ    (  195): [0097]< GPRS_REGISTRATION_STATE {1, null, null, 9}
     *    03-27 16:37:33.780 D/RILJ    (  195): [0098]< REGISTRATION_STATE {1, 4654, 0084EC08, 9, null, null, null, null, null, null, null, null, null, null, A5}
     *    03-27 16:37:33.790 D/RILJ    (  195): [0099]< QUERY_NETWORK_SELECTION_MODE {0}
     *    03-27 16:37:33.790 D/GSM     (  195): Poll ServiceState done:  oldSS=[0 home F SFR (N/A) 20810  UMTS CSS not supported -1 -1RoamInd: -1DefRoamInd: -1EmergOnly: false] newSS=[0 home F SFR (N/A) 20810  HSDPA CSS not supported -1 -1RoamInd: -1DefRoamInd: -1EmergOnly: false] oldGprs=0 newGprs=0 oldType=UMTS newType=HSDPA
     *    03-27 16:37:33.790 D/GSM     (  195): RAT switched UMTS -> HSDPA at cell 8711176
     *    03-27 16:37:41.270 D/RILJ    (  195): [UNSL]< UNSOL_RESPONSE_NETWORK_STATE_CHANGED
     *    03-27 16:37:41.270 D/RILJ    (  195): [0100]> OPERATOR
     *    03-27 16:37:41.270 D/RILJ    (  195): [0101]> GPRS_REGISTRATION_STATE
     *    03-27 16:37:41.270 D/RILJ    (  195): [0102]> REGISTRATION_STATE
     *    03-27 16:37:41.270 D/RILJ    (  195): [0103]> QUERY_NETWORK_SELECTION_MODE
     *    03-27 16:37:41.290 D/RILJ    (  195): [0100]< OPERATOR {F SFR, (N/A), 20810}
     *    03-27 16:37:41.310 D/RILJ    (  195): [0101]< GPRS_REGISTRATION_STATE {1, null, null, 3}
     *    03-27 16:37:41.340 D/RILJ    (  195): [0102]< REGISTRATION_STATE {1, 4654, 0084EC08, 3, null, null, null, null, null, null, null, null, null, null, A5}
     *    03-27 16:37:41.350 D/RILJ    (  195): [0103]< QUERY_NETWORK_SELECTION_MODE {0}
     *    03-27 16:37:41.350 D/GSM     (  195): Poll ServiceState done:  oldSS=[0 home F SFR (N/A) 20810  HSDPA CSS not supported -1 -1RoamInd: -1DefRoamInd: -1EmergOnly: false] newSS=[0 home F SFR (N/A) 20810  UMTS CSS not supported -1 -1RoamInd: -1DefRoamInd: -1EmergOnly: false] oldGprs=0 newGprs=0 oldType=HSDPA newType=UMTS
     *    03-27 16:37:41.350 D/GSM     (  195): RAT switched HSDPA -> UMTS at cell 8711176
     *    03-27 16:37:44.140 D/RILJ    (  195): [0104]> SIGNAL_STRENGTH
     *    03-27 16:37:44.150 D/RILJ    (  195): [0104]< SIGNAL_STRENGTH {4, 99, -1, -1, -1, -1, 0}
     * </pre>
     */
    RADIOLOG,

    /**
     * IS_SILENT
     * <p>
     * Since: 4.0 - Default: Yes
     * <p>
     * True if the report has been explicitly sent silently by the developer.
     */
    IS_SILENT,

    /**
     * DEVICE_ID
     * <p>
     * Since: 4.0 - Default: No
     * <p>
     * Requires READ_PHONE_STATE permission. Device unique ID (IMEI for GSM and the MEID or ESN for
     * CDMA phones). If you need to know if reports come from single or multiple users without using
     * the READ_PHONE_STATE permission, you should use #INSTALLATION_ID instead. See
     * TelephonyManager.html#getDeviceId()
     */
    DEVICE_ID,

    /**
     * INSTALLATION_ID
     * <p>
     * Since: 4.0 - Default: Yes
     * <p>
     * Installation unique ID. This identifier allow you to track a specific user application
     * installation without using any personal data. Implemented following the guidelines from the
     * Android Developers Blog.
     */
    INSTALLATION_ID,

    /**
     * USER_EMAIL
     * <p>
     * Since: 4.0 - Default: Yes
     * <p>
     * User email address. Can be provided by the user in the acra.user.email SharedPreference
     * field.
     */
    USER_EMAIL,

    /**
     * DEVICE_FEATURES
     * <p>
     * Since: 4.0 - Default: Yes
     * <p>
     * Features declared as available on this device by the system. These features are those that
     * are used by the android market to filter apps according to <uses-feature> directives from
     * applications manifests. Example from a NexusOne:
     * 
     * <pre>
     *    android.hardware.location.network
     *    android.hardware.wifi
     *    com.google.android.feature.GOOGLE_BUILD
     *    android.hardware.telephony
     *    android.hardware.location
     *    android.software.sip
     *    android.hardware.touchscreen.multitouch
     *    android.hardware.sensor.compass
     *    android.hardware.camera
     *    android.hardware.bluetooth
     *    android.hardware.sensor.proximity
     *    android.software.sip.voip
     *    android.hardware.microphone
     *    android.hardware.sensor.light
     *    android.hardware.location.gps
     *    android.hardware.camera.autofocus
     *    android.hardware.telephony.gsm
     *    android.hardware.sensor.accelerometer
     *    android.hardware.touchscreen
     *    android.software.live_wallpaper
     *    android.hardware.camera.flash
     *    glEsVersion = 2.0
     * </pre>
     */
    DEVICE_FEATURES,

    /**
     * ENVIRONMENT
     * <p>
     * Since: 4.0 - Default: Yes
     * <p>
     * External storage state and standard directories. Example from a NexusOne:
     * 
     * <pre>
     *    getDataDirectory=/data
     *    getDownloadCacheDirectory=/cache
     *    getExternalStorageAndroidDataDir=/mnt/sdcard/Android/data
     *    getExternalStorageDirectory=/mnt/sdcard
     *    getExternalStorageState=mounted
     *    getRootDirectory=/system
     *    getSecureDataDirectory=/data
     *    getSystemSecureDirectory=/data/system
     *    isEncryptedFilesystemEnabled=false
     *    isExternalStorageRemovable=true
     * </pre>
     */
    ENVIRONMENT,

    /**
     * SHARED_PREFERENCES
     * <p>
     * Since: 4.2 - Default: Yes
     * <p>
     * Collects your applications SharedPreferences settings. "empty" often means that the user did
     * not change anything so all values are default. You can add your onw SharedPreferences names
     * to be collected with
     * 
     * <pre>
     * @ReportsCrashes(additionalSharedPreferences={"my.own.prefs","a.second.prefs" ).
     * </pre>
     */
    SHARED_PREFERENCES,

    /**
     * SETTINGS_SYSTEM
     * <p>
     * Since: 4.0 - Default: Yes
     * <p>
     * System settings. Example from Nexus One:
     * 
     * <pre>
     *    ACCELEROMETER_ROTATION=1
     *    AIRPLANE_MODE_ON=0
     *    AIRPLANE_MODE_RADIOS=cell,bluetooth,wifi
     *    AIRPLANE_MODE_TOGGLEABLE_RADIOS=bluetooth,wifi
     *    ALARM_ALERT=content://media/internal/audio/media/81
     *    AUTO_TIME=0
     *    CALL_AUTO_RETRY=0
     *    CAR_DOCK_SOUND=/system/media/audio/ui/Dock.ogg
     *    CAR_UNDOCK_SOUND=/system/media/audio/ui/Undock.ogg
     *    DESK_DOCK_SOUND=/system/media/audio/ui/Dock.ogg
     *    DESK_UNDOCK_SOUND=/system/media/audio/ui/Undock.ogg
     *    DIM_SCREEN=1
     *    DOCK_SOUNDS_ENABLED=0
     *    DTMF_TONE_TYPE_WHEN_DIALING=0
     *    EMERGENCY_TONE=0
     *    FONT_SCALE=1.0
     *    HAPTIC_FEEDBACK_ENABLED=1
     *    HEARING_AID=0
     *    LOCKSCREEN_SOUNDS_ENABLED=0
     *    LOCK_SOUND=/system/media/audio/ui/Lock.ogg
     *    LOW_BATTERY_SOUND=/system/media/audio/ui/LowBattery.ogg
     *    MODE_RINGER=2
     *    MODE_RINGER_STREAMS_AFFECTED=166
     *    MUTE_STREAMS_AFFECTED=46
     *    NEXT_ALARM_FORMATTED=sam. 6:45
     *    NOTIFICATIONS_USE_RING_VOLUME=1
     *    NOTIFICATION_LIGHT_PULSE=1
     *    NOTIFICATION_SOUND=content://media/external/audio/media/85
     *    POWER_SOUNDS_ENABLED=1
     *    RINGTONE=content://media/internal/audio/media/11
     *    SCREEN_BRIGHTNESS=76
     *    SCREEN_BRIGHTNESS_MODE=1
     *    SCREEN_OFF_TIMEOUT=60000
     *    SHOW_WEB_SUGGESTIONS=1
     *    STAY_ON_WHILE_PLUGGED_IN=0
     *    TRANSITION_ANIMATION_SCALE=1.0
     *    TTY_MODE=0
     *    UNLOCK_SOUND=/system/media/audio/ui/Unlock.ogg
     *    VIBRATE_IN_SILENT=1
     *    VIBRATE_ON=5
     *    VOLUME_ALARM=5
     *    VOLUME_BLUETOOTH_SCO=9
     *    VOLUME_MUSIC=15
     *    VOLUME_NOTIFICATION=5
     *    VOLUME_RING=6
     *    VOLUME_SYSTEM=7
     *    VOLUME_VOICE=4
     *    WIFI_SLEEP_POLICY=2
     *    WIFI_USE_STATIC_IP=0
     *    WINDOW_ANIMATION_SCALE=1.0
     * </pre>
     */
    SETTINGS_SYSTEM,

    /**
     * SETTINGS_SECURE
     * <p>
     * Since: 4.0 - Default: Yes
     * <p>
     * Secure settings, applications can't modify them, only the user. Example from Nexus One:
     * 
     * <pre>
     *    ADB_ENABLED=1
     *    ALLOWED_GEOLOCATION_ORIGINS=http://www.google.co.uk http://www.google.com
     *    ALLOW_MOCK_LOCATION=0
     *    ANDROID_ID=200142d4dfd4e641
     *    ASSISTED_GPS_ENABLED=1
     *    BACKGROUND_DATA=1
     *    BACKUP_ENABLED=1
     *    BACKUP_PROVISIONED=1
     *    BACKUP_TRANSPORT=com.google.android.backup/.BackupTransportService
     *    BLUETOOTH_ON=1
     *    CDMA_CELL_BROADCAST_SMS=1
     *    DATA_ROAMING=0
     *    DEFAULT_INPUT_METHOD=com.touchtype.swiftkey/.KeyboardService
     *    DEVICE_PROVISIONED=1
     *    DISABLED_SYSTEM_INPUT_METHODS=
     *    ENABLED_ACCESSIBILITY_SERVICES=
     *    ENABLED_INPUT_METHODS=com.google.android.inputmethod.latin/com.android.inputmethod.latin.LatinIME:com.touchtype.swiftkey/.KeyboardService
     *    INSTALL_NON_MARKET_APPS=1
     *    LAST_SETUP_SHOWN=eclair_1
     *    LOCATION_PROVIDERS_ALLOWED=network,gps
     *    MOBILE_DATA=1
     *    MOUNT_PLAY_NOTIFICATION_SND=1
     *    MOUNT_UMS_AUTOSTART=0
     *    MOUNT_UMS_NOTIFY_ENABLED=1
     *    MOUNT_UMS_PROMPT=1
     *    NETWORK_PREFERENCE=1
     *    PREFERRED_CDMA_SUBSCRIPTION=1
     *    PREFERRED_NETWORK_MODE=0
     *    SEND_ACTION_APP_ERROR=1
     *    THROTTLE_RESET_DAY=14
     *    TTS_DEFAULT_COUNTRY=FRA
     *    TTS_DEFAULT_LANG=fra
     *    TTS_DEFAULT_RATE=100
     *    TTS_DEFAULT_SYNTH=com.svox.pico
     *    TTS_DEFAULT_VARIANT=
     *    TTS_ENABLED_PLUGINS=
     *    TTS_USE_DEFAULTS=0
     *    USB_MASS_STORAGE_ENABLED=1
     *    VOICE_RECOGNITION_SERVICE=com.google.android.voicesearch/.GoogleRecognitionService
     *    WIFI_NETWORKS_AVAILABLE_NOTIFICATION_ON=1
     *    WIFI_ON=0
     *    WIFI_SAVED_STATE=0
     *    WIFI_WATCHDOG_WATCH_LIST=GoogleGuest
     * </pre>
     */
    SETTINGS_SECURE;

    /**
     * @return the tag name (for Google spreadsheat use)
     */
    public String tagName() {

        return name().toLowerCase().replaceAll("[^a-z0-9]", "");
    }
}

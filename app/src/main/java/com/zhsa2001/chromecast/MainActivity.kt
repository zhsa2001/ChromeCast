package com.zhsa2001.chromecast

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.cast.MediaInfo
import com.google.android.gms.cast.MediaLoadOptions
import com.google.android.gms.cast.MediaLoadRequestData
import com.google.android.gms.cast.MediaMetadata
import com.google.android.gms.cast.framework.CastButtonFactory
import com.google.android.gms.cast.framework.CastContext
import com.google.android.gms.cast.framework.CastSession
import com.google.android.gms.cast.framework.SessionManager
import com.google.android.gms.cast.framework.SessionManagerListener
import com.zhsa2001.chromecast.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityMainBinding
    private var currentSession: CastSession? = null

    private lateinit var mCastContext: CastContext
    private lateinit var mSessionManager: SessionManager

    private val mediaSessionListener = object : SessionManagerListener<CastSession> {

        override fun onSessionStarted(session: CastSession, sessionId: String) {
            currentSession = session

            // Тут проверим, что мы готовы начать кастинг
            checkAndStartCasting()
        }

        override fun onSessionEnding(session: CastSession) {

        }

        override fun onSessionResumed(session: CastSession, wasSuspended: Boolean) {
            currentSession = session
            checkAndStartCasting()
        }

        override fun onSessionStartFailed(session: CastSession, p1: Int) {

        }

        override fun onSessionEnded(session: CastSession, p1: Int) {
            // do nothing
        }

        override fun onSessionResumeFailed(session: CastSession, p1: Int) {
            // do nothing
        }

        override fun onSessionSuspended(session: CastSession, p1: Int) {
            // do nothing
        }

        override fun onSessionStarting(session: CastSession) {
            // do nothing
        }

        override fun onSessionResuming(session: CastSession, sessionId: String) {
            // do nothing
        }
    }

    private fun checkAndStartCasting() {
        val mediaMetadata = MediaMetadata(
            MediaMetadata.MEDIA_TYPE_MUSIC_TRACK
        ).apply {
            putString(MediaMetadata.KEY_TITLE, "видео")
            putString(MediaMetadata.KEY_ARTIST, "описание")

        }

        val mediaInfo =
            MediaInfo.Builder("https://vdt-m.odkl.ru/?pct=1&expires=1740697093746&srcIp=46.138.67.120&pr=40&srcAg=GECKO&ms=185.100.104.136&type=5&sig=aGXIPmU2YtM&ct=0&clientType=45&id=228121840273")
                .setContentType("video/mp4").setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
                .setMetadata(mediaMetadata).build()

        val remoteMediaClient = currentSession?.remoteMediaClient
        remoteMediaClient?.load(
            MediaLoadRequestData.Builder().setMediaInfo(mediaInfo).setAutoplay(true).build()
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        CastButtonFactory.setUpMediaRouteButton(applicationContext, viewBinding.mediaRouteButton)

        mCastContext = CastContext.getSharedInstance(this)
        val sessionManager = mCastContext.sessionManager
        sessionManager.addSessionManagerListener(mediaSessionListener, CastSession::class.java)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onResume() {
        super.onResume()
        if (this::mSessionManager.isInitialized) {
            currentSession = mSessionManager.currentCastSession
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        if (this::mSessionManager.isInitialized) {
            mSessionManager.removeSessionManagerListener(
                mediaSessionListener, CastSession::class.java
            )
        }
    }
}

package com.piri.probodia.widget.utils

import android.content.Context
import android.util.Log
import com.piri.probodia.R
import okhttp3.OkHttpClient
import java.security.KeyStore
import java.security.SecureRandom
import java.security.cert.Certificate
import java.security.cert.CertificateException
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import java.security.cert.CertificateFactory
import javax.net.ssl.SSLSession

import javax.net.ssl.HostnameVerifier

object SSLUtil {

    fun generateSecureOkHttpClient(context : Context) : OkHttpClient {

        var httpClientBuilder = OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)

        val cf = CertificateFactory.getInstance("X.509")
        val caInput = context.resources.openRawResource(R.raw.aiserverssl)
        var ca: Certificate?
        caInput.use { caInput ->
            ca = cf.generateCertificate(caInput)
        }

        val keyStoreType = KeyStore.getDefaultType()
        val keyStore = KeyStore.getInstance(keyStoreType)
        keyStore.load(null, null)
        if (ca == null) {
            return httpClientBuilder.build()
        }
        keyStore.setCertificateEntry("ca", ca)

        val tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm()
        val tmf = TrustManagerFactory.getInstance(tmfAlgorithm)
        tmf.init(keyStore)

        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, tmf.trustManagers, SecureRandom())

        return httpClientBuilder
            .sslSocketFactory(sslContext.socketFactory)
            .hostnameVerifier(NullHostNameVerifier())
            .build()
    }

    class NullHostNameVerifier : HostnameVerifier {
        override fun verify(hostname: String, session: SSLSession): Boolean {
            return true
        }
    }
}
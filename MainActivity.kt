package com.dor.spannableexmple

import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.Spannable
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import java.util.regex.Pattern

class MainActivity : AppCompatActivity() {

    companion object {
        const val INTENT_WEB_VIEW_ACTION_PAGE = "page"
        const val PAGE_TERMS = 11
        const val PAGE_PRIVACY_POLICY = 12
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val terms = arrayOf(
            getString(R.string.read_and_agree_user_agreement_span),
            getString(R.string.read_and_agree_user_privacy_span)
        )

        setSpans(agreementTextView, getString(R.string.read_and_agree_user_agreement), terms)
    }

    private fun setSpans(textView: TextView, fulltext: String, subtext: Array<String>) {
        textView.movementMethod = LinkMovementMethod.getInstance()
        textView.minLines = 3
        textView.setText(fulltext, TextView.BufferType.SPANNABLE)
        val str = textView.text as Spannable

        for (aSubtext in subtext) {
            val word = Pattern.compile(aSubtext)
            val match = word.matcher(fulltext)

            while (match.find()) {
                str.setSpan(object : ClickableSpan() {
                    override fun onClick(widget: View) {
                        if (aSubtext == getString(R.string.read_and_agree_user_agreement_span)) {
                            startActivity(
                                Intent(this@MainActivity, WebViewActivity::class.java).apply {
                                    putExtra(INTENT_WEB_VIEW_ACTION_PAGE, PAGE_TERMS)
                                }
                            )
                        } else if (aSubtext == getString(R.string.read_and_agree_user_privacy_span)) {
                            startActivity(
                                Intent(this@MainActivity, WebViewActivity::class.java).apply {
                                    putExtra(INTENT_WEB_VIEW_ACTION_PAGE, PAGE_PRIVACY_POLICY)
                                }
                            )
                        }
                    }

                    override fun updateDrawState(ds: TextPaint) {
                        super.updateDrawState(ds)

                        ds.color = ContextCompat.getColor(this@MainActivity, R.color.colorAccent)
                        ds.isUnderlineText = true
                    }
                }, match.start(), match.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }
    }
}

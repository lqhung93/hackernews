package hackernews.propertyguru.com.hackernews.views

import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import hackernews.propertyguru.com.hackernews.network.api.PollingCenter
import hackernews.propertyguru.com.hackernews.network.responses.ErrorData
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * Created by hung on 5/8/19.
 */
open class BaseActivity : AppCompatActivity() {

    protected val pollingCenter = PollingCenter.getPollingCenter()

    public override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        EventBus.getDefault().unregister(this)
        super.onStop()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onError(error: ErrorData) {
        Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show()
    }
}

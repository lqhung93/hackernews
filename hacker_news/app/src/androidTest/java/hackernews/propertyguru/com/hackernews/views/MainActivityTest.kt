package hackernews.propertyguru.com.hackernews.views

import android.content.Intent
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.UiController
import android.support.test.espresso.ViewAction
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.contrib.RecyclerViewActions.scrollToHolder
import android.support.test.espresso.intent.Intents.intended
import android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent
import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.espresso.matcher.BoundedMatcher
import android.support.test.espresso.matcher.ViewMatchers.isEnabled
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig
import com.github.tomakehurst.wiremock.junit.WireMockRule
import com.google.gson.Gson
import hackernews.propertyguru.com.hackernews.R
import hackernews.propertyguru.com.hackernews.network.PollingCenterTest
import hackernews.propertyguru.com.hackernews.network.api.HnApiService
import hackernews.propertyguru.com.hackernews.network.responses.GetStoryDetailResponse
import hackernews.propertyguru.com.hackernews.network.responses.GetTopStoriesResponse
import hackernews.propertyguru.com.hackernews.rv.CustomRecyclerView
import hackernews.propertyguru.com.hackernews.rv.NewsAdapter
import hackernews.propertyguru.com.hackernews.utils.AssetsReader
import hackernews.propertyguru.com.hackernews.utils.C
import hackernews.propertyguru.com.hackernews.utils.Constants
import hackernews.propertyguru.com.hackernews.utils.LogUtils
import hackernews.propertyguru.com.hackernews.views.MainActivityTest.CustomMatcher.Companion.performClickView
import hackernews.propertyguru.com.hackernews.views.MainActivityTest.CustomMatcher.Companion.withItemCount
import hackernews.propertyguru.com.hackernews.views.MainActivityTest.CustomMatcher.Companion.withViewInViewHolder
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Created by hung on 5/10/19.
 */
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    private val TAG = LogUtils.makeTag(MainActivityTest::class.java)

    companion object {
        var gsonTest: Gson? = null
        var pollingCenterTest: PollingCenterTest? = null

        @BeforeClass
        @JvmStatic
        fun setUp() {
            gsonTest = HnApiService.gson
            C.END_POINT = Constants.LOCAL_HOST
            pollingCenterTest = PollingCenterTest.getPollingCenter()
        }
    }

    @Rule
    @JvmField
    var activityRule = IntentsTestRule<MainActivity>(MainActivity::class.java, true, false)

    @Rule
    @JvmField
    var wireMockRule = WireMockRule(wireMockConfig().port(Constants.PORT))


    @Test
    fun checkNewsRecyclerViewItemCount() {
        pollingCenterTest?.getTopStories()

        val getTopStoriesResponse = gsonTest?.fromJson(AssetsReader.asset("topstories.json"), GetTopStoriesResponse::class.java)
        getTopStoriesResponse?.ids?.forEach {
            pollingCenterTest?.getStoryDetail(it, "story")
        }

        reloadActivity()

        onView(withId(R.id.news_rv)).check(matches(withItemCount(getTopStoriesResponse?.ids?.size
                ?: -1)))
    }

    @Test
    fun checkOpenFirstStoryComment() {
        pollingCenterTest?.getTopStories()

        val getTopStoriesResponse = gsonTest?.fromJson(AssetsReader.asset("topstories.json"), GetTopStoriesResponse::class.java)
        getTopStoriesResponse?.ids?.forEach {
            pollingCenterTest?.getStoryDetail(it, "story")
        }

        val commentList = AssetsReader.asset("comment/list").split(",").toTypedArray()
        commentList.forEach {
            pollingCenterTest?.getStoryDetail(it, "comment")
        }

        reloadActivity()

        onView(withId(R.id.news_rv)).perform(RecyclerViewActions.actionOnItemAtPosition<NewsAdapter.RowHolder>(0, performClickView(R.id.comment_btn)))
        intended(hasComponent(CommentActivity::class.java.name))
    }

    @Test
    fun checkFirstStoryTitle() {
        pollingCenterTest?.getTopStories()

        val getTopStoriesResponse = gsonTest?.fromJson(AssetsReader.asset("topstories.json"), GetTopStoriesResponse::class.java)
        getTopStoriesResponse?.ids?.forEach {
            pollingCenterTest?.getStoryDetail(it, "story")
        }

        val getFirstStoryResponse = gsonTest?.fromJson(AssetsReader.asset("story/19884273.json"), GetStoryDetailResponse::class.java)

        reloadActivity()

        onView(withId(R.id.news_rv)).perform(scrollToHolder(withViewInViewHolder(R.id.title_tv, getFirstStoryResponse?.title.toString())))
    }

    class CustomMatcher {
        companion object {
            fun withItemCount(count: Int): Matcher<View> {
                return object : BoundedMatcher<View, CustomRecyclerView>(CustomRecyclerView::class.java) {
                    override fun describeTo(description: Description?) {
                        description?.appendText("with item count: $count")
                    }

                    override fun matchesSafely(item: CustomRecyclerView?): Boolean {
                        return item?.adapter?.itemCount == count
                    }
                }
            }

            fun withViewInViewHolder(id: Int, content: String): Matcher<RecyclerView.ViewHolder> {
                return object : BoundedMatcher<RecyclerView.ViewHolder, NewsAdapter.RowHolder>(NewsAdapter.RowHolder::class.java) {
                    override fun describeTo(description: Description?) {
                        description?.appendText("view holder with title: $content")
                    }

                    override fun matchesSafely(item: NewsAdapter.RowHolder?): Boolean {
                        return (item?.itemView?.findViewById<TextView>(id))?.text.toString() == content
                    }
                }
            }

            fun performClickView(id: Int): ViewAction {
                return object : ViewAction {
                    override fun getConstraints(): Matcher<View> {
                        return isEnabled()
                    }

                    override fun getDescription(): String {
                        return "call performClickView() method"
                    }

                    override fun perform(uiController: UiController?, view: View?) {
                        val v = view?.findViewById<View?>(id)
                        v?.performClick()
                    }
                }
            }
        }
    }

    private fun reloadActivity() {
        val intent = Intent()
        activityRule.launchActivity(intent)
    }
}
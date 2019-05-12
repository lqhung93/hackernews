package hackernews.propertyguru.com.hackernews.views

import android.content.Intent
import android.content.res.Resources
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.UiController
import android.support.test.espresso.ViewAction
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.intent.Intents.intended
import android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent
import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.espresso.matcher.BoundedMatcher
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.RecyclerView
import android.view.View
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
import hackernews.propertyguru.com.hackernews.views.MainActivityTest.CustomMatcher.Companion.atPositionOnView
import hackernews.propertyguru.com.hackernews.views.MainActivityTest.CustomMatcher.Companion.performClickView
import hackernews.propertyguru.com.hackernews.views.MainActivityTest.CustomMatcher.Companion.withItemCount
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
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
        // Load top stories list
        pollingCenterTest?.getTopStories()

        // Load all story details
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
        // Load top stories list
        pollingCenterTest?.getTopStories()

        // Load all story details
        val getTopStoriesResponse = gsonTest?.fromJson(AssetsReader.asset("topstories.json"), GetTopStoriesResponse::class.java)
        getTopStoriesResponse?.ids?.forEach {
            pollingCenterTest?.getStoryDetail(it, "story")
        }

        // Load all comment details
        val commentList = stringToWords(AssetsReader.asset("comment/list"))
        commentList.forEach {
            pollingCenterTest?.getStoryDetail(it, "comment")
        }

        reloadActivity()

        onView(withId(R.id.news_rv)).perform(RecyclerViewActions.actionOnItemAtPosition<NewsAdapter.RowHolder>(0, performClickView(R.id.comment_btn)))
        intended(hasComponent(CommentActivity::class.java.name))
    }

    @Test
    fun checkEveryStoryTitle() {
        // Load top stories list
        pollingCenterTest?.getTopStories()

        // Load all story details
        val getTopStoriesResponse = gsonTest?.fromJson(AssetsReader.asset("topstories.json"), GetTopStoriesResponse::class.java)
        getTopStoriesResponse?.ids?.forEach {
            pollingCenterTest?.getStoryDetail(it, "story")
        }

        reloadActivity()

        // Check title of all stories
        val sortedList = stringToWords(AssetsReader.asset("story/time_sorted_list"))
        for (i in 0 until sortedList.count()!!) {
            compareStoryTitle(sortedList, 1)
        }
    }

    private fun compareStoryTitle(sortedList: List<String>, position: Int) {
        val getStoryResponse = gsonTest?.fromJson(AssetsReader.asset("story/" + sortedList[position] + ".json"), GetStoryDetailResponse::class.java)
        onView(atPositionOnView(R.id.news_rv, position, R.id.title_tv)).check(matches(withText(getStoryResponse?.title)))
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

            fun atPositionOnView(recyclerViewId: Int, position: Int, targetViewId: Int): Matcher<View> {

                return object : TypeSafeMatcher<View>() {
                    internal var resources: Resources? = null
                    internal var childView: View? = null

                    override fun describeTo(description: Description) {
                        var idDescription = Integer.toString(recyclerViewId)
                        if (this.resources != null) {
                            idDescription = try {
                                this.resources!!.getResourceName(recyclerViewId)
                            } catch (var4: Exception) {
                                String.format("%s (resource name not found)", Integer.valueOf(recyclerViewId))
                            }
                        }

                        description.appendText("with id: $idDescription")
                    }

                    public override fun matchesSafely(view: View): Boolean {

                        this.resources = view.resources

                        if (childView == null) {
                            val recyclerView = view.rootView.findViewById<View>(recyclerViewId) as RecyclerView
                            if (recyclerView != null && recyclerView.id == recyclerViewId) {
                                childView = recyclerView.findViewHolderForAdapterPosition(position)!!.itemView
                            } else {
                                return false
                            }
                        }

                        if (targetViewId == -1) {
                            return view === childView
                        } else {
                            val targetView = childView!!.findViewById<View>(targetViewId)
                            return view === targetView
                        }

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

    private fun stringToWords(s: String) = s.trim().splitToSequence(',')
            .filter { it.isNotEmpty() } // or: .filter { it.isNotBlank() }
            .toList()
}

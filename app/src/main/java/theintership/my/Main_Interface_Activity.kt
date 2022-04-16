package theintership.my

import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TableLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import theintership.my.all_class.MyMethod.Companion.showToastShort
import theintership.my.main_interface.adapter.TabPageAdapter

class Main_Interface_Activity : AppCompatActivity() {


    private var isfinish_anim = true
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_interface)

//        val layout_title = findViewById<LinearLayout>(R.id.main_interface_layout_title)
        viewPager = findViewById(R.id.main_interface_viewPager)
        tabLayout = findViewById(R.id.main_interface_tabLayout)
        val welcom = "Welcom to the intership."
        welcom.showToastShort(this)
        setupTabBar()


    }

    private fun setupTabBar() {
        val adapter = TabPageAdapter(this, tabLayout.tabCount)
        viewPager.adapter = adapter

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                tabLayout.selectTab(tabLayout.getTabAt(position))
            }
        })

        tabLayout.getTabAt(4)?.setCustomView(adapter.set_custom_icon_notificaion())

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })


    }

    private fun move_layout_title_up(view: View) {
        if (view.visibility == View.GONE) {
            println("debug vao gone")
            return
        }
        val anim_up = AnimationUtils.loadAnimation(this, R.anim.move_up)
        anim_up.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) {
                view.visibility = View.VISIBLE
                isfinish_anim = false
            }

            override fun onAnimationEnd(p0: Animation?) {
                view.visibility = View.GONE
                isfinish_anim = true
            }

            override fun onAnimationRepeat(p0: Animation?) {
                TODO("Not yet implemented")
            }
        })
        println("debug finish: $isfinish_anim")
        if (isfinish_anim) {
            view.startAnimation(anim_up)
        }
    }

    private fun move_layout_title_down(view: View) {
        val anim_down = AnimationUtils.loadAnimation(this, R.anim.move_down)
        anim_down.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) {
                view.visibility = View.VISIBLE
                isfinish_anim = false
            }

            override fun onAnimationEnd(p0: Animation?) {
                isfinish_anim = true
            }

            override fun onAnimationRepeat(p0: Animation?) {
                TODO("Not yet implemented")
            }
        })
        if (isfinish_anim) {
            view.startAnimation(anim_down)
        }
    }


}
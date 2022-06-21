package qiu.li.gao.activity

import qiu.li.gao.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun getViewBinding() = ActivityMainBinding.inflate(layoutInflater)
    override fun initialization() {
        _binding.tv.text = "hello viewbinding"
    }

}
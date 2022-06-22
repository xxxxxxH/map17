package qiu.li.gao.utils

import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule

@GlideModule
class MAppGlideModule:AppGlideModule() {
    override fun isManifestParsingEnabled(): Boolean {
        return false
    }
}
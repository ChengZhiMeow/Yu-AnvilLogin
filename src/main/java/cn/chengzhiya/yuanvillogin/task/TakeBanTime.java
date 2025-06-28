package cn.chengzhiya.yuanvillogin.task;

import cn.chengzhiya.mhdfscheduler.runnable.MHDFRunnable;
import cn.chengzhiya.yuanvillogin.Main;

public final class TakeBanTime extends MHDFRunnable {
    @Override
    public void run() {
        for (String key : Main.instance.getCacheManager().keys()) {
            if (!key.endsWith("_banTime")) {
                continue;
            }

            String banTimeString = Main.instance.getCacheManager().get(key);
            if (banTimeString == null) {
                continue;
            }

            int banTime = Integer.parseInt(banTimeString);
            banTime--;
            if (banTime <= 0) {
                Main.instance.getCacheManager().remove(key);
                continue;
            }

            Main.instance.getCacheManager().put(key, String.valueOf(banTime));
        }
    }
}

package io.mithrilcoin.mithrilplay.view.adapter;

/**
 * Created by Moon on 2015-03-03.
 */
public class HistoryTimeTag extends RewardHistoryItem {

    public HistoryTimeTag(long time) {
        super(time);
    }

    public HistoryTimeTag(int year, int month, int dayOfMonth) {
        super(year, month, dayOfMonth);
    }

    @Override
    public int getType() {
        return TYPE_TIME;
    }
}

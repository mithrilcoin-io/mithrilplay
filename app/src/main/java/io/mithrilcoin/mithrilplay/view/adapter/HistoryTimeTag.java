package io.mithrilcoin.mithrilplay.view.adapter;

/**
 *  RewardHistoryAdapter item
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

package de.hysky.skyblocker.skyblock.tabhud.widget.hud;

import de.hysky.skyblocker.skyblock.dwarven.DwarvenHud;
import de.hysky.skyblocker.skyblock.tabhud.util.Ico;
import de.hysky.skyblocker.skyblock.tabhud.widget.Widget;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.text.NumberFormat;
import java.text.ParseException;

// this widget shows the status of the king's commissions.
// (dwarven mines and crystal hollows)
// USE ONLY WITH THE DWARVEN HUD!

public class HudPowderWidget extends Widget {

    /**
     * American number format
     */
    private static final NumberFormat NUMBER_FORMAT = NumberFormat.getInstance();
    /**
     * current value of Mithril Powder
     */
    private static int mithrilPowder = 0;
    /**
     * current value of Gemstone Powder
     */
    private static int gemstonePowder = 0;
    /**
     * the difference between the previous and current value of Mithril Powder
     */
    private static int mithrilPowderDif = 0;
    /**
     * the difference between the previous and current value of Gemstone Powder
     */
    private static int gemstonePowderDif = 0;
    /**
     * The initial value of the timer for the difference update delay countdown.
     */
    private static long startTime = System.currentTimeMillis();

    private static final MutableText TITLE = Text.literal("Powders").formatted(Formatting.DARK_AQUA,
            Formatting.BOLD);


    // disgusting hack to get around text renderer issues.
    // the ctor eventually tries to get the font's height, which doesn't work
    //   when called before the client window is created (roughly).
    // the rebdering god 2 from the fabricord explained that detail, thanks!
    //coppied from the HodCommsWidget to be used in the same place
    public static final HudPowderWidget INSTANCE = new HudPowderWidget();
    public static final HudPowderWidget INSTANCE_CFG = new HudPowderWidget();

    // another repulsive hack to make this widget-like hud element work with the new widget class
    // DON'T USE WITH THE WIDGET SYSTEM, ONLY USE FOR DWARVENHUD!
    public HudPowderWidget() {
        super(TITLE, Formatting.DARK_AQUA.getColorValue());
    }

    /**
     * Converts a string with a number and commas between digits to an integer value.
     * @param str a string with a number and commas between digits
     * @return integer value
     */
    private static int parsePowder(String str) {
        try {
            return NUMBER_FORMAT.parse(str).intValue();
        } catch (ParseException e) {
            return 0;
        }
    }

    /**
     * Converts Powder and difference values to a string and adds commas to the digits of the numbers.
     * @param powder the value of Mithril or Gemstone Powder
     * @param diff the difference between the previous and current value of Mithril or Gemstone Powder
     * @return formatted string
     */
    private static String formatPowderString(int powder, int diff) {
        if (diff == 0) return NUMBER_FORMAT.format(powder);
        return NUMBER_FORMAT.format(powder) + (diff > 0 ? " (+" : " (") + NUMBER_FORMAT.format(diff) + ")";
    }

    /**
     * Updates Powders and difference values when Powder values change or every 2 seconds.
     */
    private static void updatePowders(){
        long elapsedTime = System.currentTimeMillis() - startTime;

        int newMithrilPowder = parsePowder(DwarvenHud.mithrilPowder);
        int newGemstonePowder = parsePowder(DwarvenHud.gemStonePowder);

        if (newMithrilPowder != mithrilPowder || newGemstonePowder != gemstonePowder || elapsedTime > 2000) {
            startTime = System.currentTimeMillis();

            mithrilPowderDif = newMithrilPowder - mithrilPowder;
            gemstonePowderDif = newGemstonePowder - gemstonePowder;

            mithrilPowder = newMithrilPowder;
            gemstonePowder = newGemstonePowder;
        }
    }

    @Override
    public void updateContent() {
        updatePowders();
        String mithrilPowderString = formatPowderString(mithrilPowder, mithrilPowderDif);
        String gemstonePowderString = formatPowderString(gemstonePowder, gemstonePowderDif);

        this.addSimpleIcoText(Ico.MITHRIL, "Mithril: ", Formatting.AQUA, mithrilPowderString);
        this.addSimpleIcoText(Ico.AMETHYST_SHARD, "Gemstone: ", Formatting.DARK_PURPLE, gemstonePowderString);
    }

}

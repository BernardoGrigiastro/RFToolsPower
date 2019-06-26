package mcjty.rftoolspower.blocks;

import mcjty.lib.client.HudRenderHelper;
import mcjty.lib.client.RenderHelper;
import mcjty.lib.varia.EnergyTools;
import mcjty.rftoolspower.network.PacketGetMonitorLog;
import mcjty.rftoolspower.network.RFToolsPowerMessages;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.Direction;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class InformationScreenRenderer extends TileEntitySpecialRenderer<InformationScreenTileEntity> {

    @Override
    public void render(InformationScreenTileEntity te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        super.render(te, x, y, z, partialTicks, destroyStage, alpha);
        renderHud(te, x, y, z);
    }

    public static void renderHud(InformationScreenTileEntity hudinfoscreen, double x, double y, double z) {
        renderHud(hudinfoscreen, x, y, z, 0.3f, false);
    }

    public static void renderHud(InformationScreenTileEntity infoscreen, double x, double y, double z, float scale, boolean faceVert) {
        long t = System.currentTimeMillis();
        if (t - infoscreen.getLastUpdateTime() > 250) {
            RFToolsPowerMessages.INSTANCE.sendToServer(new PacketGetMonitorLog(infoscreen.getPos()));
            infoscreen.setLastUpdateTime(t);
        }
        Direction orientation = infoscreen.getBlockOrientation();
        if (orientation == null) {
            return;
        }

        EnergyTools.EnergyLevel power = infoscreen.getClientPower();
        if (infoscreen.getMode() == 0 || power == null) {
            List<String> log = getLog(power, infoscreen);
            HudRenderHelper.HudPlacement hudPlacement = HudRenderHelper.HudPlacement.HUD_FRONT;
            HudRenderHelper.HudOrientation hudOrientation = HudRenderHelper.HudOrientation.HUD_SOUTH;
            HudRenderHelper.renderHud(log, hudPlacement, hudOrientation, orientation, x - orientation.getFrontOffsetX() * .95, y, z - orientation.getFrontOffsetZ() * .95, 1.0f + scale);
        } else {
            renderGraphical(power, orientation, x - orientation.getFrontOffsetX() * .95, y, z - orientation.getFrontOffsetZ() * .95, 1.0f + scale,
                    infoscreen);
        }
    }

    private static List<String> getLog(EnergyTools.EnergyLevel power, InformationScreenTileEntity infoscreen) {
        List<String> list = new ArrayList<>();
        list.add("");

        if (power != null) {
            long energy = power.getEnergy();
            long maxEnergy = power.getMaxEnergy();
            list.add(TextFormatting.BLUE + " RF: " + TextFormatting.WHITE + formatPower(energy));
            list.add(TextFormatting.BLUE + " Max: " + TextFormatting.WHITE + formatPower(maxEnergy));
            if (infoscreen.getRfExtractPerTick() != -1) {
                list.add(TextFormatting.BLUE + " Ext/t: " + TextFormatting.WHITE + formatPower(infoscreen.getRfExtractPerTick()));
                list.add(TextFormatting.BLUE + " Ins/t: " + TextFormatting.WHITE + formatPower(infoscreen.getRfInsertedPerTick()));
            }
        } else {
            list.add(TextFormatting.RED + " Not a powercell");
            list.add(TextFormatting.RED + " or anything that");
            list.add(TextFormatting.RED + " supports power");
        }
        return list;
    }

    private static DecimalFormat format = new DecimalFormat("#.###");

    private static String formatPower(long l) {
        if (l < 100000) {
            return Long.toString(l);
        } else if (l < 10000000) {
            Double d = l / 1000.0;
            return format.format(d)+"K";
        } else if (l < 10000000000L) {
            Double d = l / 1000000.0;
            return format.format(d)+"M";
        } else {
            Double d = l / 1000000000.0;
            return format.format(d)+"G";
        }
    }

    public static void renderGraphical(EnergyTools.EnergyLevel power,
                                 Direction orientation,
                                 double x, double y, double z, float scale,
                                 InformationScreenTileEntity infoscreen) {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x + 0.5F, (float) y + 0.75F, (float) z + 0.5F);
        GlStateManager.rotate(-getHudAngle(orientation), 0.0F, 1.0F, 0.0F);
        GlStateManager.translate(0.0F, -0.2500F, -0.4375F + .9);

        net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
        Minecraft.getInstance().entityRenderer.disableLightmap();
        GlStateManager.disableBlend();
        GlStateManager.disableLighting();

        if (power.getMaxEnergy() > 0) {
            int mode = infoscreen.getMode();
            GlStateManager.translate(-0.5F, 0.5F, 0.07F);
            float f3 = 0.0075F;
            GlStateManager.scale(f3 * scale, -f3 * scale, f3);
            GlStateManager.glNormal3f(0.0F, 0.0F, 1.0F);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            long pct = power.getEnergy() * 100 / power.getMaxEnergy();
            for (int i = 0 ; i < 100 ; i += 5) {
                int col = i < pct ? getPercentageColor(i) : 0xff111111;
                RenderHelper.drawFlatBox(16, (int) (100-i*.8-13), mode == 1 ? 88 : 44, (int) (100-i*.8+3-13), col, col);
            }
            
            if (mode == 2) {
                long roughMax = infoscreen.getRoughMaxRfPerTick();
                if (roughMax > 0) {
                    long inserted = infoscreen.getRfInsertedPerTick();
                    long extracted = infoscreen.getRfExtractPerTick();

                    long pctInserted = Math.max(0, Math.min(100L, inserted * 100 / roughMax));
                    RenderHelper.drawFlatBox(60, 20, 90, 50, 0xffffffff, 0xff000000 + (int)(pctInserted * 2.5f));
                    long pctExtracted = Math.max(0, Math.min(100L, extracted * 100 / roughMax));
                    int mask = (int) (pctExtracted * 2.5f);
                    RenderHelper.drawFlatBox(60, 54, 90, 84, 0xffffffff, 0xff000000 + (mask << 16) + (mask << 8));
                }
            }
        }
        Minecraft.getInstance().entityRenderer.enableLightmap();

//        RenderHelper.enableStandardItemLighting();
        GlStateManager.enableLighting();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        GlStateManager.popMatrix();
    }

    private static int getPercentageColor(int i) {
        int col;
        if (i < 30) {
            col = 0xff00ff00;
        } else if (i < 40) {
            col = 0xff22dd00;
        } else if (i < 50) {
            col = 0xff44bb00;
        } else if (i < 60) {
            col = 0xff669900;
        } else if (i < 70) {
            col = 0xff887700;
        } else if (i < 80) {
            col = 0xffaa5500;
        } else if (i < 90) {
            col = 0xffcc3300;
        } else {
            col = 0xffee1100;
        }
        return col;
    }

    private static float getHudAngle(Direction orientation) {
        float f3 = 0.0f;

        if (orientation != null) {
            switch (orientation) {
                case NORTH:
                    f3 = 180.0F;
                    break;
                case WEST:
                    f3 = 90.0F;
                    break;
                case EAST:
                    f3 = -90.0F;
                    break;
                default:
                    f3 = 0.0f;
            }
        }
        return f3;
    }

    public static void register() {
        ClientRegistry.bindTileEntitySpecialRenderer(InformationScreenTileEntity.class, new InformationScreenRenderer());
    }
}

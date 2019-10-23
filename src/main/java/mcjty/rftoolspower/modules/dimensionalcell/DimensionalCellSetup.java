package mcjty.rftoolspower.modules.dimensionalcell;

import mcjty.lib.blocks.BaseBlockItem;
import mcjty.lib.container.GenericContainer;
import mcjty.rftoolspower.RFToolsPower;
import mcjty.rftoolspower.modules.dimensionalcell.blocks.*;
import mcjty.rftoolspower.modules.dimensionalcell.items.PowerCellCardItem;
import net.minecraft.block.Block;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.ObjectHolder;

public class DimensionalCellSetup {

    @ObjectHolder(RFToolsPower.MODID + ":dimensionalcell")
    public static DimensionalCellBlock DIMENSIONAL_CELL;
    @ObjectHolder(RFToolsPower.MODID + ":dimensionalcell_advanced")
    public static DimensionalCellBlock DIMENSIONAL_CELL_ADVANCED;
    @ObjectHolder(RFToolsPower.MODID + ":dimensionalcell_creative")
    public static DimensionalCellBlock DIMENSIONAL_CELL_CREATIVE;
    @ObjectHolder(RFToolsPower.MODID + ":dimensionalcell_simple")
    public static DimensionalCellBlock DIMENSIONAL_CELL_SIMPLE;

    @ObjectHolder(RFToolsPower.MODID + ":dimensionalcell")
    public static TileEntityType<?> TYPE_DIMENSIONAL_CELL;
    @ObjectHolder(RFToolsPower.MODID + ":dimensionalcell_advanced")
    public static TileEntityType<?> TYPE_DIMENSIONAL_CELL_ADVANCED;
    @ObjectHolder(RFToolsPower.MODID + ":dimensionalcell_creative")
    public static TileEntityType<?> TYPE_DIMENSIONAL_CELL_CREATIVE;
    @ObjectHolder(RFToolsPower.MODID + ":dimensionalcell_simple")
    public static TileEntityType<?> TYPE_DIMENSIONAL_CELL_SIMPLE;

    @ObjectHolder(RFToolsPower.MODID + ":powercell_card")
    public static PowerCellCardItem POWERCELL_CARD;

    @ObjectHolder(RFToolsPower.MODID + ":dimensionalcell")
    public static ContainerType<DimensionalCellContainer> CONTAINER_DIMENSIONAL_CELL;

    public static void registerBlocks(final RegistryEvent.Register<Block> event) {
        event.getRegistry().register(new DimensionalCellBlock("dimensionalcell", DimensionalCellType.NORMAL, DimensionalCellNormalTileEntity::new));
        event.getRegistry().register(new DimensionalCellBlock("dimensionalcell_advanced", DimensionalCellType.ADVANCED, DimensionalCellAdvancedTileEntity::new));
        event.getRegistry().register(new DimensionalCellBlock("dimensionalcell_creative", DimensionalCellType.CREATIVE, DimensionalCellCreativeTileEntity::new));
        event.getRegistry().register(new DimensionalCellBlock("dimensionalcell_simple", DimensionalCellType.SIMPLE, DimensionalCellSimpleTileEntity::new));
    }

    public static void registerItems(final RegistryEvent.Register<Item> event) {
        Item.Properties properties = new Item.Properties().group(RFToolsPower.setup.getTab());
        event.getRegistry().register(new BaseBlockItem(DIMENSIONAL_CELL, properties));
        event.getRegistry().register(new BaseBlockItem(DIMENSIONAL_CELL_ADVANCED, properties));
        event.getRegistry().register(new BaseBlockItem(DIMENSIONAL_CELL_CREATIVE, properties));
        event.getRegistry().register(new BaseBlockItem(DIMENSIONAL_CELL_SIMPLE, properties));
        event.getRegistry().register(new PowerCellCardItem());
    }

    public static void registerTiles(final RegistryEvent.Register<TileEntityType<?>> event) {
        event.getRegistry().register(TileEntityType.Builder.create(DimensionalCellNormalTileEntity::new, DIMENSIONAL_CELL).build(null).setRegistryName("dimensionalcell"));
        event.getRegistry().register(TileEntityType.Builder.create(DimensionalCellAdvancedTileEntity::new, DIMENSIONAL_CELL_ADVANCED).build(null).setRegistryName("dimensionalcell_advanced"));
        event.getRegistry().register(TileEntityType.Builder.create(DimensionalCellCreativeTileEntity::new, DIMENSIONAL_CELL_CREATIVE).build(null).setRegistryName("dimensionalcell_creative"));
        event.getRegistry().register(TileEntityType.Builder.create(DimensionalCellSimpleTileEntity::new, DIMENSIONAL_CELL_SIMPLE).build(null).setRegistryName("dimensionalcell_simple"));
    }

    public static void registerContainers(final RegistryEvent.Register<ContainerType<?>> event) {
        event.getRegistry().register(GenericContainer.createContainerType("dimensionalcell"));
    }
}
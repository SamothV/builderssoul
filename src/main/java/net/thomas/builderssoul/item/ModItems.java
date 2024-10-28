package net.thomas.builderssoul.item;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.levelgen.structure.structures.OceanMonumentPieces;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;
import net.thomas.builderssoul.BuildersSoul;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import java.util.Random;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;



public class ModItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, BuildersSoul.MOD_ID);

    public static final RegistryObject<Item> BLOCKPLACER = ITEMS.register("blockplacer", BlockPlacerItem::new);

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

    public static class BlockPlacerItem extends Item {
        public BlockPlacerItem(){
            super(new Item.Properties());
        }

        @Override
        public InteractionResult useOn(UseOnContext context) {
            Level world = context.getLevel();
            BlockPos pos = context.getClickedPos().relative(context.getClickedFace());

            // Loop over a 3x3x3 area centered on the `pos`
            Random random = new Random();

            //main type of block
            Block[] buildingMaterials = {
                    Blocks.STONE,
                    Blocks.COBBLESTONE,
                    Blocks.BRICKS,
                    Blocks.SANDSTONE,
                    Blocks.RED_SANDSTONE,
                    Blocks.QUARTZ_BLOCK,
                    Blocks.TERRACOTTA,
                    Blocks.WHITE_TERRACOTTA,
                    Blocks.BLACKSTONE,
            };
            Block buidlingBlock = buildingMaterials[random.nextInt(buildingMaterials.length)];


            //Willekeurige lengtes
            int length = random.nextInt(4)+4;
            int width = random.nextInt(4)+4;
            int height = random.nextInt(2)+2;

            BlockPos doorPos;

            if (width%2!=0) {
                doorPos = pos.offset(0, 0, length/2); //Door location
            }
            else{
                doorPos = pos.offset(0, 0, length/2);
            }
            //Voorste muur

            for (int z = 0; z <= length; z++) {
                for (int y = 0; y <= height; y++) {
                    BlockPos newPos = pos.offset(0, y, z);
                    // Check if the block at newPos is empty, if position is same as door don't place block, and then place wood
                    if (newPos != doorPos && newPos != doorPos.above()) {
                        world.setBlockAndUpdate(newPos, buidlingBlock.defaultBlockState());
                    }
                }
            }

            //achterste muur

            for (int z = 0; z <= length; z++) {
                for (int y = 0; y <= height; y++) {
                    BlockPos newPos = pos.offset(width, y, z);
                    world.setBlockAndUpdate(newPos, buidlingBlock.defaultBlockState());
                }
            }



            for (int x = 0; x <= width; x++) {
                for (int y = 0; y <= height; y++) {
                    BlockPos newPos = pos.offset(x, y, 0);
                    world.setBlockAndUpdate(newPos, buidlingBlock.defaultBlockState());
                }
            }


            for (int x = 0; x <= width; x++) {
                for (int y = 0; y <= height; y++) {
                    BlockPos newPos = pos.offset(x, y, length);
                    world.setBlockAndUpdate(newPos, buidlingBlock.defaultBlockState());
                }
            }

            //dak
            for (int x=0; x <= width; x++){
                for (int z=0; z<=length;z++){
                    BlockPos newPos = pos.offset(x, height, z);

                    world.setBlockAndUpdate(newPos, buidlingBlock.defaultBlockState());


                }
            }
            //vloer
            for (int x=1; x <= width-1; x++){
                for (int z=1; z<=length-1;z++){
                    BlockPos newPos = pos.offset(x, -1, z);
                    world.setBlockAndUpdate(newPos, Blocks.COBBLESTONE.defaultBlockState());
                }
            }

            Block[] doorTypes = {
                    Blocks.OAK_DOOR,
                    Blocks.BIRCH_DOOR,
                    Blocks.SPRUCE_DOOR,
                    Blocks.JUNGLE_DOOR,
                    Blocks.ACACIA_DOOR,
                    Blocks.DARK_OAK_DOOR,
                    Blocks.IRON_DOOR
            };
            Block selectedDoor = doorTypes[random.nextInt(doorTypes.length)];

            world.setBlockAndUpdate(doorPos.above(), selectedDoor.defaultBlockState().setValue(DoorBlock.HALF, DoubleBlockHalf.UPPER).setValue(DoorBlock.OPEN, true));
            world.setBlockAndUpdate(doorPos, selectedDoor.defaultBlockState().setValue(DoorBlock.OPEN, true));



            return InteractionResult.SUCCESS;
        }
    }
}

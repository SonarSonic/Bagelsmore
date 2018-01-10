package sonar.bagels.common.blocks;

import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import sonar.bagels.api.IDeskPart;
import sonar.bagels.common.tileentity.TileBookshelf;
import sonar.core.common.block.SonarMaterials;
import sonar.core.common.block.properties.SonarProperties;
import sonar.core.integration.multipart.BlockFacingMultipart;
import sonar.core.network.FlexibleGuiHandler;

public class BlockBookshelf extends BlockFacingMultipart implements IDeskPart {

	public static final PropertyInteger books = PropertyInteger.create("books", 0, 7);

	public BlockBookshelf() {
		super(SonarMaterials.droppable_wood);
	}

	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		EnumFacing face = state.getValue(SonarProperties.ORIENTATION);
		double p = 0.0625;
		double minY = 1 - p / 2;
		double maxY = 1 + p * 5 + p / 2;
		double maxX = p * 15;
		double minX = p * 1;
		switch (face) {
		case EAST:
			return new AxisAlignedBB(p * 5 + p / 2, minY, minX, p / 2, maxY, maxX);
		case NORTH:
			return new AxisAlignedBB(minX, minY, 1 - p * 5 + p / 2, maxX, maxY, 1 - p * 1 + p / 2);
		case SOUTH:
			return new AxisAlignedBB(minX, minY, p * 5 + p / 2, maxX, maxY, p * 1 + p / 2);
		case WEST:
			return new AxisAlignedBB(1 - p * 5 + p / 2, minY, minX, 1 - p * 1 + p / 2, maxY, maxX);
		default:
			return super.getBoundingBox(state, source, pos);
		}
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!world.isRemote) {
			FlexibleGuiHandler.openMultipartGui(0, player, world, pos);
		}
		return true;
	}

	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, SonarProperties.ORIENTATION, books);
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
		// return state.withProperty(SonarProperties.ORIENTATION, face).withProperty(book1, bookCount > 0).withProperty(book2, bookCount > 1).withProperty(book3, bookCount > 2).withProperty(book4, bookCount > 3).withProperty(book5, bookCount > 4).withProperty(book6, bookCount > 5).withProperty(book7, bookCount > 6);
		int bookCount = 0;
		TileEntity tile = world.getTileEntity(pos);
		if (tile != null && tile instanceof TileBookshelf) {
			bookCount = ((TileBookshelf) tile).books.getObject();
		}
		return state.withProperty(books, bookCount);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileBookshelf();
	}

}

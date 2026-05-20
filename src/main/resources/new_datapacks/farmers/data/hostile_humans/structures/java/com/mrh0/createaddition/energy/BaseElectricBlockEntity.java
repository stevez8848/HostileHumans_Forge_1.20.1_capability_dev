package com.mrh0.createaddition.energy;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

import com.machinezoo.noexception.optional.OptionalSupplier;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
//import net.minecraftforge.common.capabilities.Capability;
//import net.minecraftforge.common.capabilities.ForgeCapabilities;
//import net.minecraftforge.common.util.LazyOptional;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;

public abstract class BaseElectricBlockEntity extends SmartBlockEntity {

	protected final InternalEnergyStorage localEnergy;
	protected Optional<IEnergyStorage> lazyEnergy;

	private EnumSet<Direction> invalidSides = EnumSet.allOf(Direction.class);
	private EnumMap<Direction, IEnergyStorage> escacheMap = new EnumMap<>(Direction.class);

	public BaseElectricBlockEntity(BlockEntityType<?> tileEntityTypeIn, BlockPos pos, BlockState state) {
		super(tileEntityTypeIn, pos, state);
		localEnergy = new InternalEnergyStorage(getCapacity(), getMaxIn(), getMaxOut());
		lazyEnergy = Optional.of(/*() -> */localEnergy);
		setLazyTickRate(20);
	}

	public abstract int getCapacity();
	public abstract int getMaxIn();
	public abstract int getMaxOut();

	@Override
	public void addBehaviours(List<BlockEntityBehaviour> behaviours) {}

//	@Override
//	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
//		if(cap == ForgeCapabilities.ENERGY && (isEnergyInput(side) || isEnergyOutput(side)))// && !level.isClientSide
//			return lazyEnergy.cast();
//		return super.getCapability(cap, side);
//	}

	public abstract boolean isEnergyInput(Direction side);
	public abstract boolean isEnergyOutput(Direction side);

	@Override
	protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean arg1) {
		super.read(compound, registries, arg1);
		localEnergy.read(compound);
	}

	@Override
	public void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
		super.write(compound, registries, clientPacket);
		localEnergy.write(compound);
	}

	@Override
	public void remove() {
//		lazyEnergy.invalidate();
	}

	@Deprecated
	public void outputTick(int max) {
		for(Direction side : Direction.values()) {
			if(!isEnergyOutput(side))
				continue;
			localEnergy.outputToSide(level, worldPosition, side, max);
		}
	}

	@Override
	public void tick() {
		super.tick();
		if(!invalidSides.isEmpty()) {
			invalidSides.forEach(this::updateCache);
			invalidSides.clear();
		}
	}

	public boolean ignoreCapSide() {
		return false;
	}

	private void invalidCache(Direction side) {
		invalidSides.add(side);
	}

	public void updateCache() {
		if(level.isClientSide())
			return;
		for(Direction side : Direction.values()) {
			updateCache(side);
		}
	}

	public void updateCache(Direction side) {
		if (!level.isLoaded(worldPosition.relative(side))) {
			setCache(side, null);
			return;
		}
		BlockEntity te = level.getBlockEntity(worldPosition.relative(side));
		if(te == null) {
			setCache(side, null);
			return;
		}
		IEnergyStorage le = level.getCapability(Capabilities.EnergyStorage.BLOCK, worldPosition.relative(side), side.getOpposite());
		if(ignoreCapSide() && le == null) le = level.getCapability(Capabilities.EnergyStorage.BLOCK, worldPosition, side.getOpposite());
		// Make sure the side isn't already cached.
		if (le.equals(getCachedEnergy(side))) return;
		setCache(side, le);
//		le.addListener((es) -> invalidCache(side));
	}

	public void setCache(Direction side, IEnergyStorage storage) {
		escacheMap.put(side, storage);
	}

	public IEnergyStorage getCachedEnergy(Direction side) {
		return escacheMap.getOrDefault(side, null);
	}
}

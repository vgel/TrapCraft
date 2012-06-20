package net.minecraft.trapcraft;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.DamageSource;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityPotion;
import net.minecraft.src.Item;
import net.minecraft.src.ItemPotion;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;
import net.minecraft.src.ModLoader;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.Potion;
import net.minecraft.src.PotionEffect;
import net.minecraft.src.PotionHealth;
import net.minecraft.src.PotionHelper;
import net.minecraft.src.Vec3D;
import net.minecraft.src.World;

public class EntityDart extends Entity {
	private int xTile;
	private int yTile;
	private int zTile;
	private int inTile;
	private int inData;
	private boolean inGround;
	public boolean doesArrowBelongToPlayer;

	/** Seems to be some sort of timer for animating an arrow. */
	public int arrowShake;

	/** The owner of this arrow. */
	public Entity shootingEntity;
	private int ticksInGround;
	private int ticksInAir;
	private double damage;
	private int field_46027_au;

	/** Is this arrow a critical hit? (Controls particles and damage) */
	public boolean arrowCritical;

	ItemStack dartStack;

	public EntityDart(World par1World) {
		super(par1World);
		xTile = -1;
		yTile = -1;
		zTile = -1;
		inTile = 0;
		inData = 0;
		inGround = false;
		doesArrowBelongToPlayer = false;
		arrowShake = 0;
		ticksInAir = 0;
		damage = 2D;
		arrowCritical = false;
		setSize(0.5F, 0.5F);
	}

	public EntityDart(World par1World, double par2, double par4, double par6) {
		super(par1World);
		xTile = -1;
		yTile = -1;
		zTile = -1;
		inTile = 0;
		inData = 0;
		inGround = false;
		doesArrowBelongToPlayer = false;
		arrowShake = 0;
		ticksInAir = 0;
		damage = 2D;
		arrowCritical = false;
		setSize(0.5F, 0.5F);
		setPosition(par2, par4, par6);
		yOffset = 0.0F;
	}

	public EntityDart(World par1World, EntityLiving par2EntityLiving, EntityLiving par3EntityLiving, float par4, float par5) {
		super(par1World);
		xTile = -1;
		yTile = -1;
		zTile = -1;
		inTile = 0;
		inData = 0;
		inGround = false;
		doesArrowBelongToPlayer = false;
		arrowShake = 0;
		ticksInAir = 0;
		damage = 2D;
		arrowCritical = false;
		shootingEntity = par2EntityLiving;
		doesArrowBelongToPlayer = par2EntityLiving instanceof EntityPlayer;
		posY = (par2EntityLiving.posY + (double) par2EntityLiving.getEyeHeight()) - 0.10000000149011612D;
		double d = par3EntityLiving.posX - par2EntityLiving.posX;
		double d1 = (par3EntityLiving.posY + (double) par3EntityLiving.getEyeHeight()) - 0.69999998807907104D - posY;
		double d2 = par3EntityLiving.posZ - par2EntityLiving.posZ;
		double d3 = MathHelper.sqrt_double(d * d + d2 * d2);

		if (d3 < 9.9999999999999995E-008D) {
			return;
		} else {
			float f = (float) ((Math.atan2(d2, d) * 180D) / Math.PI) - 90F;
			float f1 = (float) (-((Math.atan2(d1, d3) * 180D) / Math.PI));
			double d4 = d / d3;
			double d5 = d2 / d3;
			setLocationAndAngles(par2EntityLiving.posX + d4, posY, par2EntityLiving.posZ + d5, f, f1);
			yOffset = 0.0F;
			float f2 = (float) d3 * 0.2F;
			setArrowHeading(d, d1 + (double) f2, d2, par4, par5);
			return;
		}
	}

	public EntityDart(World par1World, EntityLiving par2EntityLiving, float par3) {
		super(par1World);
		xTile = -1;
		yTile = -1;
		zTile = -1;
		inTile = 0;
		inData = 0;
		inGround = false;
		doesArrowBelongToPlayer = false;
		arrowShake = 0;
		ticksInAir = 0;
		damage = 2D;
		arrowCritical = false;
		shootingEntity = par2EntityLiving;
		doesArrowBelongToPlayer = par2EntityLiving instanceof EntityPlayer;
		setSize(0.5F, 0.5F);
		setLocationAndAngles(par2EntityLiving.posX, par2EntityLiving.posY + (double) par2EntityLiving.getEyeHeight(),
				par2EntityLiving.posZ, par2EntityLiving.rotationYaw, par2EntityLiving.rotationPitch);
		posX -= MathHelper.cos((rotationYaw / 180F) * (float) Math.PI) * 0.16F;
		posY -= 0.10000000149011612D;
		posZ -= MathHelper.sin((rotationYaw / 180F) * (float) Math.PI) * 0.16F;
		setPosition(posX, posY, posZ);
		yOffset = 0.0F;
		motionX = -MathHelper.sin((rotationYaw / 180F) * (float) Math.PI) * MathHelper.cos((rotationPitch / 180F) * (float) Math.PI);
		motionZ = MathHelper.cos((rotationYaw / 180F) * (float) Math.PI) * MathHelper.cos((rotationPitch / 180F) * (float) Math.PI);
		motionY = -MathHelper.sin((rotationPitch / 180F) * (float) Math.PI);
		setArrowHeading(motionX, motionY, motionZ, par3 * 1.5F, 1.0F);
	}

	protected void entityInit() {
	}

	public void setDartStack(ItemStack dart) {
		this.dartStack = dart;
	}

	/**
	 * Uses the provided coordinates as a heading and determines the velocity from it with the set force and random
	 * variance. Args: x, y, z, force, forceVariation
	 */
	public void setArrowHeading(double par1, double par3, double par5, float par7, float par8) {
		float f = MathHelper.sqrt_double(par1 * par1 + par3 * par3 + par5 * par5);
		par1 /= f;
		par3 /= f;
		par5 /= f;
		par1 += rand.nextGaussian() * 0.0074999998323619366D * (double) par8;
		par3 += rand.nextGaussian() * 0.0074999998323619366D * (double) par8;
		par5 += rand.nextGaussian() * 0.0074999998323619366D * (double) par8;
		par1 *= par7;
		par3 *= par7;
		par5 *= par7;
		motionX = par1;
		motionY = par3;
		motionZ = par5;
		float f1 = MathHelper.sqrt_double(par1 * par1 + par5 * par5);
		prevRotationYaw = rotationYaw = (float) ((Math.atan2(par1, par5) * 180D) / Math.PI);
		prevRotationPitch = rotationPitch = (float) ((Math.atan2(par3, f1) * 180D) / Math.PI);
		ticksInGround = 0;
	}

	/**
	 * Sets the velocity to the args. Args: x, y, z
	 */
	public void setVelocity(double par1, double par3, double par5) {
		motionX = par1;
		motionY = par3;
		motionZ = par5;

		if (prevRotationPitch == 0.0F && prevRotationYaw == 0.0F) {
			float f = MathHelper.sqrt_double(par1 * par1 + par5 * par5);
			prevRotationYaw = rotationYaw = (float) ((Math.atan2(par1, par5) * 180D) / Math.PI);
			prevRotationPitch = rotationPitch = (float) ((Math.atan2(par3, f) * 180D) / Math.PI);
			prevRotationPitch = rotationPitch;
			prevRotationYaw = rotationYaw;
			setLocationAndAngles(posX, posY, posZ, rotationYaw, rotationPitch);
			ticksInGround = 0;
		}
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	public void onUpdate() {
		super.onUpdate();

		if (prevRotationPitch == 0.0F && prevRotationYaw == 0.0F) {
			float f = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
			prevRotationYaw = rotationYaw = (float) ((Math.atan2(motionX, motionZ) * 180D) / Math.PI);
			prevRotationPitch = rotationPitch = (float) ((Math.atan2(motionY, f) * 180D) / Math.PI);
		}

		int i = worldObj.getBlockId(xTile, yTile, zTile);

		if (i > 0) {
			Block.blocksList[i].setBlockBoundsBasedOnState(worldObj, xTile, yTile, zTile);
			AxisAlignedBB axisalignedbb = Block.blocksList[i].getCollisionBoundingBoxFromPool(worldObj, xTile, yTile, zTile);

			if (axisalignedbb != null && axisalignedbb.isVecInside(Vec3D.createVector(posX, posY, posZ))) {
				inGround = true;
			}
		}

		if (arrowShake > 0) {
			arrowShake--;
		}

		ticksInAir++;
		Vec3D vec3d = Vec3D.createVector(posX, posY, posZ);
		Vec3D vec3d1 = Vec3D.createVector(posX + motionX, posY + motionY, posZ + motionZ);
		MovingObjectPosition movingobjectposition = worldObj.rayTraceBlocks_do_do(vec3d, vec3d1, false, true);
		vec3d = Vec3D.createVector(posX, posY, posZ);
		vec3d1 = Vec3D.createVector(posX + motionX, posY + motionY, posZ + motionZ);

		if (movingobjectposition != null) {
			vec3d1 = Vec3D.createVector(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord,
					movingobjectposition.hitVec.zCoord);
		}

		Entity entity = null;
		List list = worldObj.getEntitiesWithinAABBExcludingEntity(this,
				boundingBox.addCoord(motionX, motionY, motionZ).expand(1.0D, 1.0D, 1.0D));
		double d = 0.0D;

		for (int l = 0; l < list.size(); l++) {
			Entity entity1 = (Entity) list.get(l);

			if (!entity1.canBeCollidedWith() || entity1 == shootingEntity && ticksInAir < 5) {
				continue;
			}

			float f5 = 0.3F;
			AxisAlignedBB axisalignedbb1 = entity1.boundingBox.expand(f5, f5, f5);
			MovingObjectPosition movingobjectposition1 = axisalignedbb1.calculateIntercept(vec3d, vec3d1);

			if (movingobjectposition1 == null) {
				continue;
			}

			double d1 = vec3d.distanceTo(movingobjectposition1.hitVec);

			if (d1 < d || d == 0.0D) {
				entity = entity1;
				d = d1;
			}
		}

		if (entity != null) {
			movingobjectposition = new MovingObjectPosition(entity);
		}

		if (movingobjectposition != null) {
			if (movingobjectposition.entityHit != null) {
				List<EntityLiving> apply = new ArrayList<EntityLiving>();
				if (ItemPotion.isSplash(dartStack.getItemDamage())) {
					for (Object e : ModLoader.getMinecraftInstance().theWorld.loadedEntityList) {
						if (e instanceof EntityLiving && ((Entity) e).getDistanceSqToEntity(movingobjectposition.entityHit) < 16) {
							apply.add((EntityLiving) e);
						}
					}
				} else {
					if (movingobjectposition.entityHit instanceof EntityLiving)
						apply.add((EntityLiving) movingobjectposition.entityHit);
				}
				for (EntityLiving e : apply) {
					applyPotionEffects(e);
				}
				setDead();
			} else {
				xTile = movingobjectposition.blockX;
				yTile = movingobjectposition.blockY;
				zTile = movingobjectposition.blockZ;
				inTile = worldObj.getBlockId(xTile, yTile, zTile);
				inData = worldObj.getBlockMetadata(xTile, yTile, zTile);
				motionX = (float) (movingobjectposition.hitVec.xCoord - posX);
				motionY = (float) (movingobjectposition.hitVec.yCoord - posY);
				motionZ = (float) (movingobjectposition.hitVec.zCoord - posZ);
				float f2 = MathHelper.sqrt_double(motionX * motionX + motionY * motionY + motionZ * motionZ);
				posX -= (motionX / (double) f2) * 0.05000000074505806D;
				posY -= (motionY / (double) f2) * 0.05000000074505806D;
				posZ -= (motionZ / (double) f2) * 0.05000000074505806D;
				worldObj.playSoundAtEntity(this, "random.bowhit", 1.0F, 1.2F / (rand.nextFloat() * 0.2F + 0.9F));
				inGround = true;
				arrowShake = 7;
				arrowCritical = false;
				List<EntityLiving> apply = new ArrayList<EntityLiving>();
				if (ItemPotion.isSplash(dartStack.getItemDamage())) {
					for (Object e : ModLoader.getMinecraftInstance().theWorld.loadedEntityList) {
						if (e instanceof EntityLiving && getDistanceSqToEntity((Entity) e) < 16) {
							apply.add((EntityLiving) e);
						}
					}
				}
				for (EntityLiving e : apply) {
					applyPotionEffects(e);
				}
				setDead();
			}
		}

		posX += motionX;
		posY += motionY;
		posZ += motionZ;
		float f3 = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
		rotationYaw = (float) ((Math.atan2(motionX, motionZ) * 180D) / Math.PI);

		for (rotationPitch = (float) ((Math.atan2(motionY, f3) * 180D) / Math.PI); rotationPitch - prevRotationPitch < -180F; prevRotationPitch -= 360F) {
		}

		for (; rotationPitch - prevRotationPitch >= 180F; prevRotationPitch += 360F) {
		}

		for (; rotationYaw - prevRotationYaw < -180F; prevRotationYaw -= 360F) {
		}

		for (; rotationYaw - prevRotationYaw >= 180F; prevRotationYaw += 360F) {
		}

		rotationPitch = prevRotationPitch + (rotationPitch - prevRotationPitch) * 0.2F;
		rotationYaw = prevRotationYaw + (rotationYaw - prevRotationYaw) * 0.2F;
		float f4 = 0.99F;
		float f6 = 0.05F;

		if (isInWater()) {
			for (int k1 = 0; k1 < 4; k1++) {
				float f8 = 0.25F;
				worldObj.spawnParticle("bubble", posX - motionX * (double) f8, posY - motionY * (double) f8, posZ - motionZ * (double) f8,
						motionX, motionY, motionZ);
			}

			f4 = 0.8F;
		}

		motionX *= f4;
		motionY *= f4;
		motionZ *= f4;
		motionY -= f6;
		setPosition(posX, posY, posZ);
	}

	void applyPotionEffects(EntityLiving e) {
		double d = getDistanceSqToEntity(e);
		if (!worldObj.isRemote) {
			double d1 = 1.0D - Math.sqrt(d) / 4D;
			List list = Item.potion.getEffects(dartStack.getItemDamage());

			if (list != null && !list.isEmpty()) {
				Iterator iterator1 = list.iterator();
				while (iterator1.hasNext()) {
					PotionEffect potioneffect = (PotionEffect) iterator1.next();
					int i = potioneffect.getPotionID();

					if (Potion.potionTypes[i].isInstant()) {
						if (shootingEntity instanceof EntityLiving)
							Potion.potionTypes[i].affectEntity((EntityLiving) shootingEntity, e, potioneffect.getAmplifier(), d1);
						else
							Potion.potionTypes[i].affectEntity(null, e, potioneffect.getAmplifier(), d1);
					} else {
						int j = (int) (d1 * (double) potioneffect.getDuration() + 0.5D);

						if (j > 20) {
							e.addPotionEffect(new PotionEffect(i, j, potioneffect.getAmplifier()));
						}
					}
				}
			}
		}
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
		par1NBTTagCompound.setShort("xTile", (short) xTile);
		par1NBTTagCompound.setShort("yTile", (short) yTile);
		par1NBTTagCompound.setShort("zTile", (short) zTile);
		par1NBTTagCompound.setByte("inTile", (byte) inTile);
		par1NBTTagCompound.setByte("inData", (byte) inData);
		par1NBTTagCompound.setByte("shake", (byte) arrowShake);
		par1NBTTagCompound.setByte("inGround", (byte) (inGround ? 1 : 0));
		par1NBTTagCompound.setBoolean("player", doesArrowBelongToPlayer);
		par1NBTTagCompound.setDouble("damage", damage);
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
		xTile = par1NBTTagCompound.getShort("xTile");
		yTile = par1NBTTagCompound.getShort("yTile");
		zTile = par1NBTTagCompound.getShort("zTile");
		inTile = par1NBTTagCompound.getByte("inTile") & 0xff;
		inData = par1NBTTagCompound.getByte("inData") & 0xff;
		arrowShake = par1NBTTagCompound.getByte("shake") & 0xff;
		inGround = par1NBTTagCompound.getByte("inGround") == 1;
		doesArrowBelongToPlayer = par1NBTTagCompound.getBoolean("player");

		if (par1NBTTagCompound.hasKey("damage")) {
			damage = par1NBTTagCompound.getDouble("damage");
		}
	}

	/**
	 * Called by a player entity when they collide with an entity
	 */
	public void onCollideWithPlayer(EntityPlayer par1EntityPlayer) {
		if (worldObj.isRemote) {
			return;
		}

		if (inGround && doesArrowBelongToPlayer && arrowShake <= 0
				&& par1EntityPlayer.inventory.addItemStackToInventory(new ItemStack(Item.arrow, 1))) {
			worldObj.playSoundAtEntity(this, "random.pop", 0.2F, ((rand.nextFloat() - rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
			par1EntityPlayer.onItemPickup(this, 1);
			setDead();
		}
	}

	public float getShadowSize() {
		return 0.0F;
	}

	public void setDamage(double par1) {
		damage = par1;
	}

	public double getDamage() {
		return damage;
	}

	public void func_46023_b(int par1) {
		field_46027_au = par1;
	}

	/**
	 * If returns false, the item will not inflict any damage against entities.
	 */
	public boolean canAttackWithItem() {
		return false;
	}
}

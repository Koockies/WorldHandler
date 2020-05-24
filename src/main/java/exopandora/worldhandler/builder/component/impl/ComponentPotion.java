package exopandora.worldhandler.builder.component.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Nullable;

import exopandora.worldhandler.builder.component.IBuilderComponent;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.potion.Effect;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class ComponentPotion implements IBuilderComponent 
{
	protected final Map<Effect, EffectNBT> potions = new HashMap<Effect, EffectNBT>();
	
	@Override
	@Nullable
	public INBT serialize()
	{
		ListNBT list = new ListNBT();
		
		for(Entry<Effect, EffectNBT> entry : this.potions.entrySet())
		{
			EffectNBT effect = entry.getValue();
			
			if(effect.getAmplifier() > 0)
			{
				CompoundNBT compound = effect.serialize();
				compound.putByte("Id", (byte) Effect.getId(entry.getKey()));
				list.add(compound);
			}
		}
		
		if(list.isEmpty())
		{
			return null;
		}
		
		return list;
	}
	
	public void setAmplifier(Effect potion, byte amplifier)
	{
		this.getMetadata(potion).setAmplifier(amplifier);
	}
	
	public byte getAmplifier(Effect potion)
	{
		return this.getMetadata(potion).getAmplifier();
	}
	
	public void setSeconds(Effect potion, int seconds)
	{
		this.getMetadata(potion).setSeconds(seconds);
	}
	
	public int getSeconds(Effect potion)
	{
		return this.getMetadata(potion).getSeconds();
	}
	
	public void setMinutes(Effect potion, int minutes)
	{
		this.getMetadata(potion).setMinutes(minutes);
	}
	
	public int getMinutes(Effect potion)
	{
		return this.getMetadata(potion).getMinutes();
	}
	
	public void setHours(Effect potion, int hours)
	{
		this.getMetadata(potion).setHours(hours);
	}
	
	public int getHours(Effect potion)
	{
		return this.getMetadata(potion).getHours();
	}
	
	public void setShowParticles(Effect potion, boolean showParticles)
	{
		this.getMetadata(potion).setShowParticles(showParticles);
	}
	
	public boolean getShowParticles(Effect potion)
	{
		return this.getMetadata(potion).getShowParticles();
	}
	
	public void setAmbient(Effect potion, boolean ambient)
	{
		this.getMetadata(potion).setAmbient(ambient);
	}
	
	public boolean getAmbient(Effect potion)
	{
		return this.getMetadata(potion).getAmbient();
	}
	
	private EffectNBT getMetadata(Effect potion)
	{
		return this.potions.get(this.validate(potion));
	}
	
	private Effect validate(Effect potion)
	{
		if(!this.potions.containsKey(potion))
		{
			this.potions.put(potion, new EffectNBT());
		}
		
		return potion;
	}
	
	public Set<Effect> getEffects()
	{
		return this.potions.keySet();
	}
	
	public void remove(Effect potion)
	{
		this.potions.remove(potion);
	}
}

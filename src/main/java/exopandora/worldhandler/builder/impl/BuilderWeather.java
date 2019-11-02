package exopandora.worldhandler.builder.impl;

import exopandora.worldhandler.builder.CommandBuilder;
import exopandora.worldhandler.builder.CommandSyntax;
import exopandora.worldhandler.builder.types.ArgumentType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BuilderWeather extends CommandBuilder
{
	public BuilderWeather()
	{
		
	}
	
	public BuilderWeather(EnumWeather weather)
	{
		this.setWeather(weather);
	}
	
	public BuilderWeather(EnumWeather weather, int value)
	{
		this(weather);
		this.setValue(value);
	}
	
	public void setWeather(EnumWeather weather)
	{
		this.setNode(0, weather.toString());
	}
	
	public void setValue(int value)
	{
		this.setNode(1, value);
	}
	
	@Override
	public String getCommandName()
	{
		return "weather";
	}

	@Override
	public final CommandSyntax getSyntax()
	{
		CommandSyntax syntax = new CommandSyntax();
		
		syntax.addRequired("clear|rain|thunde", ArgumentType.STRING);
		syntax.addOptional("duration", ArgumentType.INT);
		
		return syntax;
	}
	
	@OnlyIn(Dist.CLIENT)
	public static enum EnumWeather
	{
		CLEAR,
		RAIN,
		THUNDER;
		
		@Override
		public String toString()
		{
			return this.name().toLowerCase();
		}
	}
}

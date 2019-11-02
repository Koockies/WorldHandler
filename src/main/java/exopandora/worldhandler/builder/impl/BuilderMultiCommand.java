package exopandora.worldhandler.builder.impl;

import java.util.Arrays;

import exopandora.worldhandler.builder.ICommandBuilder;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BuilderMultiCommand implements ICommandBuilder
{
	private final ICommandBuilder[] builders;
	
	public BuilderMultiCommand(ICommandBuilder... builders)
	{
		this.builders = builders;
	}
	
	@Override
	public String toCommand()
	{
		return String.join(" | ", Arrays.stream(this.builders).map(ICommandBuilder::toCommand).toArray(String[]::new));
	}
}

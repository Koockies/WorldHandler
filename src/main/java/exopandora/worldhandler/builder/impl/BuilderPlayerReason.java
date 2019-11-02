package exopandora.worldhandler.builder.impl;

import exopandora.worldhandler.builder.CommandBuilder;
import exopandora.worldhandler.builder.CommandSyntax;
import exopandora.worldhandler.builder.types.ArgumentType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BuilderPlayerReason extends CommandBuilder
{
	private final String command;
	
	public BuilderPlayerReason(String command)
	{
		this.command = command;
	}
	
	public void setPlayer(String player)
	{
		this.setNode(0, player);
	}
	
	public String getPlayer()
	{
		return this.getNodeAsString(0);
	}
	
	public void setReason(String reason)
	{
		this.setNode(1, reason);
	}
	
	public String getReason()
	{
		return this.getNodeAsString(1);
	}
	
	@Override
	public String getCommandName()
	{
		return this.command;
	}
	
	@Override
	public final CommandSyntax getSyntax()
	{
		CommandSyntax syntax = new CommandSyntax();
		
		syntax.addRequired("player", ArgumentType.STRING);
		syntax.addOptional("reason", ArgumentType.STRING);
		
		return syntax;
	}
}

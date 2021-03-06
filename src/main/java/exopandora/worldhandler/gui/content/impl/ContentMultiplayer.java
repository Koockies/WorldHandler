package exopandora.worldhandler.gui.content.impl;

import com.google.common.base.Predicates;

import exopandora.worldhandler.builder.ICommandBuilder;
import exopandora.worldhandler.builder.impl.BuilderGeneric;
import exopandora.worldhandler.builder.impl.BuilderMultiCommand;
import exopandora.worldhandler.builder.impl.BuilderPlayer;
import exopandora.worldhandler.builder.impl.BuilderPlayerReason;
import exopandora.worldhandler.builder.impl.BuilderWhitelist;
import exopandora.worldhandler.builder.impl.BuilderWhitelist.EnumMode;
import exopandora.worldhandler.gui.button.EnumIcon;
import exopandora.worldhandler.gui.button.GuiButtonBase;
import exopandora.worldhandler.gui.button.GuiButtonIcon;
import exopandora.worldhandler.gui.button.GuiButtonTooltip;
import exopandora.worldhandler.gui.button.GuiTextFieldTooltip;
import exopandora.worldhandler.gui.category.Categories;
import exopandora.worldhandler.gui.category.Category;
import exopandora.worldhandler.gui.container.Container;
import exopandora.worldhandler.gui.container.impl.GuiWorldHandler;
import exopandora.worldhandler.gui.content.Content;
import exopandora.worldhandler.gui.content.Contents;
import exopandora.worldhandler.util.ActionHelper;
import exopandora.worldhandler.util.CommandHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ContentMultiplayer extends Content
{
	private GuiTextFieldTooltip playerField;
	private GuiTextFieldTooltip reasonField;
	
	private Page page = Page.KICK_AND_BAN;
	
	private final BuilderPlayerReason builderKick = new BuilderPlayerReason("kick");
	private final BuilderPlayerReason builderBan = new BuilderPlayerReason("ban");
	private final BuilderPlayer builderPardon = new BuilderPlayer("pardon");
	private final BuilderPlayer builderOp = new BuilderPlayer("op");
	private final BuilderPlayer builderDeop = new BuilderPlayer("deop");
	private final BuilderGeneric builderSaveAll = new BuilderGeneric("save-all");
	private final BuilderGeneric builderSaveOn = new BuilderGeneric("save-on");
	private final BuilderGeneric builderSaveOff = new BuilderGeneric("save-off");
	private final BuilderGeneric builderStop = new BuilderGeneric("stop");
	private final BuilderWhitelist builderWhitelist = new BuilderWhitelist();
	
	private final BuilderMultiCommand builderKickBan = new BuilderMultiCommand(this.builderKick, this.builderBan);
	private final BuilderMultiCommand builderPermissions = new BuilderMultiCommand(this.builderOp, this.builderDeop);
	private final BuilderMultiCommand builderRuntime = new BuilderMultiCommand(this.builderSaveAll, this.builderSaveOn, this.builderSaveOff, this.builderStop);
	
	@Override
	public ICommandBuilder getCommandBuilder()
	{
		if(Page.KICK_AND_BAN.equals(this.page))
		{
			return this.builderKickBan;
		}
		else if(Page.PARDON.equals(this.page))
		{
			return this.builderPardon;
		}
		else if(Page.PERMISSIONS.equals(this.page))
		{
			return this.builderPermissions;
		}
		else if(Page.RUNTIME.equals(this.page))
		{
			return this.builderRuntime;
		}
		else if(Page.WHITELIST.equals(this.page))
		{
			return this.builderWhitelist;
		}
		
		return null;
	}
	
	@Override
	public void initGui(Container container, int x, int y)
	{
		this.playerField = new GuiTextFieldTooltip(x + 118, y + this.page.getShift(), 114, 20, I18n.format("gui.worldhandler.multiplayer.username"));
		this.playerField.setValidator(Predicates.notNull());
		this.playerField.setFocused2(false);
		this.playerField.setText(this.builderKick.getPlayer());
		this.playerField.setMaxStringLength(16);
		this.playerField.setResponder(text ->
		{
			this.setPlayer(this.playerField.getText());
			container.initButtons();
		});
		
		this.reasonField = new GuiTextFieldTooltip(x + 118, y + 24 + this.page.getShift(), 114, 20, I18n.format("gui.worldhandler.multiplayer.kick_ban.reason"));
		this.reasonField.setValidator(Predicates.notNull());
		this.reasonField.setFocused2(false);
		this.reasonField.setText(this.builderKick.getReason());
		this.reasonField.setResponder(text ->
		{
			this.setReason(this.reasonField.getText());
			container.initButtons();
		});
	}
	
	@Override
	public void initButtons(Container container, int x, int y)
	{
		GuiButtonBase button1;
		GuiButtonBase button2;
		GuiButtonBase button3;
		GuiButtonBase button4;
		GuiButtonBase button5;
		GuiButtonBase button6;
		GuiButtonBase button7;
		
		container.add(new GuiButtonBase(x + 118, y + 96, 114, 20, I18n.format("gui.worldhandler.generic.backToGame"), ActionHelper::backToGame));
		
		container.add(button1 = new GuiButtonBase(x, y, 114, 20, I18n.format("gui.worldhandler.multiplayer.kick") + " / " + I18n.format("gui.worldhandler.multiplayer.ban"), () ->
		{
			this.page = Page.KICK_AND_BAN;
			container.init();
		}));
		container.add(button2 = new GuiButtonBase(x, y + 24, 114, 20, I18n.format("gui.worldhandler.multiplayer.pardon"), () ->
		{
			this.page = Page.PARDON;
			container.init();
		}));
		container.add(button3 = new GuiButtonBase(x, y + 48, 114, 20, I18n.format("gui.worldhandler.multiplayer.permissions"), () ->
		{
			this.page = Page.PERMISSIONS;
			container.init();
		}));
		container.add(button4 = new GuiButtonBase(x, y + 72, 114, 20, I18n.format("gui.worldhandler.multiplayer.runtime"), () ->
		{
			this.page = Page.RUNTIME;
			container.init();
		}));
		container.add(button5 = new GuiButtonBase(x, y + 96, 114, 20, I18n.format("gui.worldhandler.multiplayer.whitelist"), () ->
		{
			this.page = Page.WHITELIST;
			container.init();
		}));
		
		if(Page.KICK_AND_BAN.equals(this.page))
		{
			container.add(this.playerField);
			container.add(this.reasonField);
			container.add(button6 = new GuiButtonTooltip(x + 118, y + 48, 114, 20, I18n.format("gui.worldhandler.multiplayer.kick"), this.builderKick.toActualCommand(), () ->
			{
				CommandHelper.sendCommand(this.builderKick);
			}));
			container.add(button7 = new GuiButtonTooltip(x + 118, y + 72, 114, 20, I18n.format("gui.worldhandler.multiplayer.ban"), this.builderBan.toActualCommand(), () ->
			{
				CommandHelper.sendCommand(this.builderBan);
			}));
			
			if(this.playerField.getText().isEmpty())
			{
				button6.active = false;
				button7.active = false;
			}
			
			button1.active = false;
		}
		else if(Page.PARDON.equals(this.page))
		{
			container.add(this.playerField);
			container.add(button6 = new GuiButtonTooltip(x + 118, y + 48, 114, 20, I18n.format("gui.worldhandler.multiplayer.pardon"), this.builderPardon.toActualCommand(), () ->
			{
				CommandHelper.sendCommand(this.builderPardon);
			}));
			
			if(this.playerField.getText().isEmpty())
			{
				button6.active = false;
			}
			
			button2.active = false;
		}
		else if(Page.PERMISSIONS.equals(this.page))
		{
			container.add(this.playerField);
			container.add(button6 = new GuiButtonTooltip(x + 118, y + 24 + 12, 114, 20, I18n.format("gui.worldhandler.multiplayer.permissions.give"), this.builderOp.toActualCommand(), () ->
			{
				CommandHelper.sendCommand(this.builderOp);
			}));
			container.add(button7 = new GuiButtonTooltip(x + 118, y + 48 + 12, 114, 20, I18n.format("gui.worldhandler.multiplayer.permissions.take"), this.builderDeop.toActualCommand(), () ->
			{
				CommandHelper.sendCommand(this.builderDeop);
			}));
			
			if(this.playerField.getText().isEmpty())
			{
				button6.active = false;
				button7.active = false;
			}
			
			button3.active = false;
		}
		else if(Page.RUNTIME.equals(this.page))
		{
			container.add(new GuiButtonTooltip(x + 118, y, 114, 20, I18n.format("gui.worldhandler.multiplayer.runtime.save_world"), this.builderSaveAll.toActualCommand(), () ->
			{
				CommandHelper.sendCommand(this.builderSaveAll);
			}));
			container.add(new GuiButtonTooltip(x + 118, y + 24, 114, 20, I18n.format("gui.worldhandler.multiplayer.runtime.autosave", I18n.format("gui.worldhandler.generic.on")), this.builderSaveOn.toActualCommand(), () ->
			{
				CommandHelper.sendCommand(this.builderSaveOn);
			}));
			container.add(new GuiButtonTooltip(x + 118, y + 48, 114, 20, TextFormatting.RED + I18n.format("gui.worldhandler.multiplayer.runtime.autosave", I18n.format("gui.worldhandler.generic.off")), this.builderSaveOff.toActualCommand(), () ->
			{
				Minecraft.getInstance().displayGuiScreen(new GuiWorldHandler(Contents.CONTINUE.withBuilder(this.builderSaveOff).withParent(Contents.MULTIPLAYER)));
			}));
			container.add(new GuiButtonTooltip(x + 118, y + 72, 114, 20, TextFormatting.RED + I18n.format("gui.worldhandler.multiplayer.runtime.stop_server"), this.builderStop.toActualCommand(), () ->
			{
				Minecraft.getInstance().displayGuiScreen(new GuiWorldHandler(Contents.CONTINUE.withBuilder(this.builderStop).withParent(Contents.MULTIPLAYER)));
			}));
			
			button4.active = false;
		}
		else if(Page.WHITELIST.equals(this.page))
		{
			container.add(this.playerField);
			container.add(button6 = new GuiButtonBase(x + 118, y + 24, 44, 20, I18n.format("gui.worldhandler.multiplayer.whitelist.add"), () ->
			{
				CommandHelper.sendCommand(this.builderWhitelist.getBuilder(EnumMode.ADD));
			}));
			container.add(button7 = new GuiButtonBase(x + 118 + 47, y + 24, 44, 20, I18n.format("gui.worldhandler.multiplayer.whitelist.remove"), () ->
			{
				CommandHelper.sendCommand(this.builderWhitelist.getBuilder(EnumMode.REMOVE));
			}));
			
			container.add(new GuiButtonBase(x + 118, y + 48, 114, 20, I18n.format("gui.worldhandler.multiplayer.whitelist.whitelist", I18n.format("gui.worldhandler.generic.on")), () ->
			{
				CommandHelper.sendCommand(this.builderWhitelist.getBuilder(EnumMode.ON));
			}));
			container.add(new GuiButtonBase(x + 118, y + 72, 114, 20, I18n.format("gui.worldhandler.multiplayer.whitelist.whitelist", I18n.format("gui.worldhandler.generic.off")), () ->
			{
				CommandHelper.sendCommand(this.builderWhitelist.getBuilder(EnumMode.OFF));
			}));
			
			container.add(new GuiButtonIcon(x + 232 - 20, y + 24, 20, 20, EnumIcon.RELOAD, I18n.format("gui.worldhandler.multiplayer.whitelist.reload"), () ->
			{
				CommandHelper.sendCommand(this.builderWhitelist.getBuilder(EnumMode.RELOAD));
			}));
			
			if(this.playerField.getText().isEmpty())
			{
				button6.active = false;
				button7.active = false;
			}
			
			button5.active = false;
		}
	}
	
	@Override
	public void tick(Container container)
	{
		if(Page.KICK_AND_BAN.equals(this.page))
		{
			this.reasonField.tick();
		}
		
		if(!Page.RUNTIME.equals(this.page))
		{
			this.playerField.tick();
		}
	}
	
	@Override
	public void drawScreen(Container container, int x, int y, int mouseX, int mouseY, float partialTicks)
	{
		if(Page.KICK_AND_BAN.equals(this.page))
		{
			this.reasonField.renderButton(mouseX, mouseY, partialTicks);
		}
		
		if(!Page.RUNTIME.equals(this.page))
		{
			this.playerField.renderButton(mouseX, mouseY, partialTicks);
		}
	}
	
	private void setPlayer(String player)
	{
		this.builderBan.setPlayer(player);
		this.builderKick.setPlayer(player);
		
		this.builderPardon.setPlayer(player);
		this.builderOp.setPlayer(player);
		this.builderDeop.setPlayer(player);
		
		this.builderWhitelist.setPlayer(player);
	}
	
	private void setReason(String reason)
	{
		this.builderBan.setReason(reason);
		this.builderKick.setReason(reason);
	}
	
	@Override
	public Category getCategory()
	{
		return Categories.MAIN;
	}
	
	@Override
	public String getTitle()
	{
		return I18n.format("gui.worldhandler.title.multiplayer");
	}
	
	@Override
	public String getTabTitle()
	{
		return I18n.format("gui.worldhandler.tab.multiplayer");
	}
	
	@Override
	public Content getActiveContent()
	{
		return Contents.MULTIPLAYER;
	}
	
	@Override
	public Content getBackContent()
	{
		return null;
	}
	
	@OnlyIn(Dist.CLIENT)
	public static enum Page
	{
		KICK_AND_BAN(0),
		PARDON(24),
		PERMISSIONS(14),
		RUNTIME(0),
		WHITELIST(0);
		
		private final int shift;
		
		private Page(int shift)
		{
			this.shift = shift;
		}
		
		public int getShift()
		{
			return this.shift;
		}
	}
}

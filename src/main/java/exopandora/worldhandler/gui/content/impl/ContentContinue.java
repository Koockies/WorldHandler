package exopandora.worldhandler.gui.content.impl;

import com.mojang.blaze3d.matrix.MatrixStack;

import exopandora.worldhandler.builder.ICommandBuilder;
import exopandora.worldhandler.builder.ICommandBuilderSyntax;
import exopandora.worldhandler.gui.button.GuiButtonBase;
import exopandora.worldhandler.gui.button.GuiTextFieldTooltip;
import exopandora.worldhandler.gui.container.Container;
import exopandora.worldhandler.gui.container.impl.GuiWorldHandler;
import exopandora.worldhandler.util.ActionHelper;
import exopandora.worldhandler.util.CommandHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ContentContinue extends ContentChild
{
	private ICommandBuilder builder;
	private GuiTextFieldTooltip commandField;
	private boolean special;
	
	public ContentContinue withBuilder(ICommandBuilder builder)
	{
		return this.withBuilder(builder, false);
	}
	
	public ContentContinue withBuilder(ICommandBuilder builder, boolean special)
	{
		this.builder = builder;
		this.special = special;
		return this;
	}
	
	@Override
	public ICommandBuilder getCommandBuilder()
	{
		return this.builder;
	}
	
	@Override
	public void initGui(Container container, int x, int y)
	{
		this.commandField = new GuiTextFieldTooltip(x + 116 / 2, y + 12, 116, 20);
		this.commandField.setFocused2(false);
		
		if(this.builder instanceof ICommandBuilderSyntax)
		{
			this.commandField.setText(((ICommandBuilderSyntax) this.builder).toActualCommand());
		}
		else
		{
			this.commandField.setText(this.builder.toCommand());
		}
		
		this.commandField.setCursorPositionZero();
		this.commandField.setValidator(text -> text.equals(this.commandField.getText()));
	}
	
	@Override
	public void initButtons(Container container, int x, int y)
	{
		container.add(new GuiButtonBase(x, y + 96, 114, 20, new TranslationTextComponent("gui.worldhandler.generic.back"), () -> ActionHelper.back(this)));
		container.add(new GuiButtonBase(x + 118, y + 96, 114, 20, new TranslationTextComponent("gui.worldhandler.generic.backToGame"), ActionHelper::backToGame));
		
		container.add(this.commandField);
		container.add(new GuiButtonBase(x + 116 / 2, y + 36, 116, 20, new TranslationTextComponent("gui.worldhandler.generic.yes").func_240699_a_(TextFormatting.RED), () ->
		{
			CommandHelper.sendCommand(this.builder, this.special);
			Minecraft.getInstance().displayGuiScreen(new GuiWorldHandler(this.getParentContent()));
		}));
		container.add(new GuiButtonBase(x + 116 / 2, y + 60, 116, 20, new TranslationTextComponent("gui.worldhandler.generic.no"), () -> ActionHelper.back(this)));
	}
	
	@Override
	public void drawScreen(MatrixStack matrix, Container container, int x, int y, int mouseX, int mouseY, float partialTicks)
	{
		this.commandField.func_230431_b_(matrix, mouseX, mouseY, partialTicks); //renderButton
	}
}

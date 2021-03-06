package exopandora.worldhandler.usercontent.model;

import com.google.gson.annotations.SerializedName;

import exopandora.worldhandler.usercontent.model.JsonMenu.Type;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class JsonMenu extends JsonWidget<Type>
{
	@SerializedName("type")
	private Type type;
	
	public JsonMenu(Type type, Action action, JsonDimensions dimensions, Attributes attributes)
	{
		super(action, dimensions, attributes);
		this.type = type;
	}
	
	public Type getType()
	{
		return this.type;
	}
	
	public void setType(Type type)
	{
		this.type = type;
	}
	
	@OnlyIn(Dist.CLIENT)
	public static enum Type
	{
		PAGE_LIST;
	}
	
	@Override
	public void validate() throws IllegalStateException
	{
		if(this.type == null)
		{
			throw new IllegalStateException("menu.type is null");
		}
		
		if(this.type == Type.PAGE_LIST)
		{
			if(this.getAttributes() == null)
			{
				throw new IllegalStateException("menu.attributes is null");
			}
			else if(this.getAttributes().getId() == null)
			{
				throw new IllegalStateException("menu.attributes.id is null");
			}
			else if(this.getAttributes().getId().isEmpty())
			{
				throw new IllegalStateException("menu.attributes.id is empty");
			}
			else if(this.getAttributes().getItems() == null)
			{
				throw new IllegalStateException("menu.attributes.items is null");
			}
			else if(this.getAttributes().getItems().isEmpty())
			{
				throw new IllegalStateException("menu.attributes.items is empty");
			}
			
			this.validateAction(Action.Type.SET, Action.Type.JS);
		}
	}
}

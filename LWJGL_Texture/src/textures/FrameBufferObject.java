package textures;

import java.nio.*;
import java.util.Stack;

import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.opengl.GLContext;

public class FrameBufferObject
{
	private static Stack<FrameBufferObject> mFrameBufferStack = new Stack<FrameBufferObject>();
	
	private int mFBO;
	private Texture mTexture;
	private boolean mIsComplete = false;
	
	public FrameBufferObject() throws Exception
	{
		boolean FBOEnabled = GLContext.getCapabilities().GL_EXT_framebuffer_object;
		
		if (!FBOEnabled)
			throw new Exception("FBO capabilities not enabled!");
		
		IntBuffer buffer = ByteBuffer.allocateDirect(1*4).order(ByteOrder.nativeOrder()).asIntBuffer();
		EXTFramebufferObject.glGenFramebuffersEXT(buffer);
		mFBO = buffer.get(0);
	}
	
	public FrameBufferObject(Texture t) throws Exception
	{
		this();
		
		AssignTexture(t);
	}
	
	public void AssignTexture(Texture t) throws Exception
	{
		mIsComplete = false;
		
		mTexture = t;
		
		Bind();
		
		EXTFramebufferObject.glFramebufferTexture2DEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT,
				EXTFramebufferObject.GL_COLOR_ATTACHMENT0_EXT,
				GL_TEXTURE_2D, mTexture.ID(), 0);
		
		CheckForCompleteness();
		
		BindNext();
	}
	
	public void CheckForCompleteness() throws Exception
	{
		int framebuffer = EXTFramebufferObject.glCheckFramebufferStatusEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT);
		switch (framebuffer)
		{
			case EXTFramebufferObject.GL_FRAMEBUFFER_COMPLETE_EXT:
				mIsComplete = true;
				break;
			case EXTFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT_EXT:
				throw new RuntimeException( "FrameBuffer: " + mFBO
						+ ", has caused a GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT_EXT exception" );
			case EXTFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT_EXT:
				throw new RuntimeException( "FrameBuffer: " + mFBO
						+ ", has caused a GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT_EXT exception" );
			case EXTFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS_EXT:
				throw new RuntimeException( "FrameBuffer: " + mFBO
						+ ", has caused a GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS_EXT exception" );
			case EXTFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER_EXT:
				throw new RuntimeException( "FrameBuffer: " + mFBO
						+ ", has caused a GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER_EXT exception" );
			case EXTFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_FORMATS_EXT:
				throw new RuntimeException( "FrameBuffer: " + mFBO
						+ ", has caused a GL_FRAMEBUFFER_INCOMPLETE_FORMATS_EXT exception" );
			case EXTFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER_EXT:
				throw new RuntimeException( "FrameBuffer: " + mFBO
						+ ", has caused a GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER_EXT exception" );
			default:
				throw new RuntimeException( "Unexpected reply from glCheckFramebufferStatusEXT: " + framebuffer );
		}
	}
	
	public boolean IsComplete()
	{
		return mIsComplete;
	}
	
	public void Delete()
	{
		EXTFramebufferObject.glDeleteFramebuffersEXT(mFBO);
	}
	
	public void Begin()
	{
		if (mIsComplete)
		{
			mFrameBufferStack.push(this);
			Bind();
		}
	}
	
	public void BindNext()
	{
		if (mFrameBufferStack.empty())
		{
			BindScreen();
		}
		else
		{
			mFrameBufferStack.peek().Bind();
		}
	}
	
	public void End()
	{
		if (mIsComplete)
		{
			mFrameBufferStack.pop();
			
			BindNext();
		}
	}
	
	public void Clear()
	{
		glPushAttrib(GL_COLOR_BUFFER_BIT);
		{
			glClearColor(0, 0, 0, 0);
			glClear(GL_COLOR_BUFFER_BIT);
		}
		glPopAttrib();
	}
	
	public void Bind()
	{
		EXTFramebufferObject.glBindFramebufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, mFBO);
		glViewport(0, 0, mTexture.Width(), mTexture.Height());
	}
	
	public static void BindScreen()
	{
		EXTFramebufferObject.glBindFramebufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, 0);
	}
}

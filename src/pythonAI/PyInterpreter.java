package pythonAI;

import java.io.OutputStream;
import java.util.ArrayList;

import org.python.core.PyException;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

/**
 * This classloader restricts all imports in your script.
 */
class PyClassLoader extends ClassLoader
{
	/**
	 * Throw an exception when the script attempts to import.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Class findClass(String p_name) throws ClassNotFoundException
	{
		throw new ClassNotFoundException("Imports are not allowed");
	}
}

public class PyInterpreter
{
	private PythonInterpreter m_interpreter;
	private ArrayList<PyInterpreterCallback> m_callback = new ArrayList<PyInterpreterCallback>();
	private boolean m_ready = false;
	private String m_script = "";
	
	public PyInterpreter()
	{
		m_interpreter = new PythonInterpreter();
	}
	
	public void reinitialize()
	{
		try
		{
			for (PyInterpreterCallback i : m_callback)
				i.onBeginReinitialize();
			
			System.out.println("Loading Python Interpreter...");
			
			// Reset the interpreter
			m_interpreter.cleanup();
			// Remove access to other classes in the project
			m_interpreter.getSystemState().setClassLoader(new PyClassLoader());
			
			System.out.println("Python Interpreter Ready");
			System.out.println("Compiling script...");
			
			// Interpret the global scope of the script
			m_interpreter.exec(m_script);
			
			System.out.println("Script Compiled");
			
			m_ready = true;
			
			for (PyInterpreterCallback i : m_callback)
				i.onEndReinitialize();
		}
		catch(PyException e)
		{
			for (PyInterpreterCallback i : m_callback)
				i.onException(e);
			m_ready = false;
		}
	}

	public boolean isReady()
	{
		return m_ready;
	}
	
	public void setScript(String p_text)
	{
		m_script = p_text;
	}

	public String getScript()
	{
		return  m_script;
	}

	public void addCallback(PyInterpreterCallback p_callback)
	{
		m_callback.add(p_callback);
	}
	
	public void setOutputStream(OutputStream p_outputStream)
	{
		m_interpreter.setOut(p_outputStream);
		m_interpreter.setErr(p_outputStream);
	}
	
	public String getGlobalString(String p_name)
	{
		try
		{
			PyObject obj = m_interpreter.get(p_name);
			if (obj == null)
				return null;
			return obj.asString();
		}
		catch(PyException e)
		{
			for (PyInterpreterCallback i : m_callback)
				i.onException(e);
			return null;
		}
	}
	
	public PyObject getGlobal(String p_name)
	{
		try
		{
			PyObject obj = m_interpreter.get(p_name);
			if (obj == null)
				return null;
			return obj;
		}
		catch(PyException e)
		{
			for (PyInterpreterCallback i : m_callback)
				i.onException(e);
			return null;
		}
	}
	
	public void call(PyObject p_obj, Object[] p_params)
	{
		try
		{
			p_obj._jcall(p_params);
		}
		catch(PyException e)
		{
			for (PyInterpreterCallback i : m_callback)
				i.onException(e);
			m_ready = false;
		}
	}
}

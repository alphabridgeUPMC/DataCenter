package fr.upmc.datacenter.hardware.processors;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import fr.upmc.datacenter.hardware.processors.interfaces.ProcessorStaticStateI;

/**
 * The class <code>ProcessorStaticState</code> implements objects representing
 * a snapshot of the static state of a processor component to be pulled or
 * pushed through the dynamic state data interface.
 *
 * <p><strong>Description</strong></p>
 * 
 * <p><strong>Invariant</strong></p>
 * 
 * TODO: complete!
 * 
 * <pre>
 * invariant	timestamp >= 0 && timestamperIP != null
 * invariant	numberOfCores > 0
 * invariant	admissibleFrequencies != null && forall i in admissibleFrequencies, i > 0
 * invariant	admissibleFrequencies.contains(defaultFrequency)
 * invariant	processingPower != null && forall i in processingPower.values(), i > 0
 * </pre>
 * 
 * <p>Created on : 7 avr. 2015</p>
 * 
 * @author	<a href="mailto:Jacques.Malenfant@lip6.fr">Jacques Malenfant</a>
 * @version	$Name$ -- $Revision$ -- $Date$
 */
public class			ProcessorStaticState
implements	ProcessorStaticStateI
{
	// ------------------------------------------------------------------------
	// Instance variables and constants
	// ------------------------------------------------------------------------

	private static final long 				serialVersionUID = 1L ;
	/** timestamp in Unix time format, local time of the timestamper.		*/
	protected final long					timestamp ;
	/** IP of the node that did the timestamping.							*/
	protected final String					timestamperIP ;
	/** number of cores of the processor.									*/
	protected final int						numberOfCores ;
	/** default frequency at which the cores run.							*/
	protected final int						defaultFrequency ;
	/** max frequency gap among cores of the same processor.				*/
	protected final int						maxFrequencyGap ;
	/** admissible frequencies for cores.									*/
	protected final Set<Integer>			admissibleFrequencies ;
	/** Mips for the different admissible frequencies.						*/
	protected final Map<Integer, Integer>	processingPower ;

	// ------------------------------------------------------------------------
	// Constructors
	// ------------------------------------------------------------------------

	/**
	 * create a snapshot of the static state of a processor component.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	numberOfCores > 0
	 * pre	admissibleFrequencies != null && forall i in admissibleFrequencies, i > 0
	 * pre	admissibleFrequencies.contains(defaultFrequency)
	 * pre	processingPower != null && forall i in processingPower.values(), i > 0
	 * post	true			// no postcondition.
	 * </pre>
	 *
	 * @param numberOfCores			number of cores of the processor.
	 * @param defaultFrequency		default frequency at which the cores run.
	 * @param maxFrequencyGap		max frequency gap among cores of the same processor.
	 * @param admissibleFrequencies	admissible frequencies for cores.
	 * @param processingPower		Mips for the different admissible frequencies.
	 * @throws Exception
	 */
	public				ProcessorStaticState(
		int numberOfCores,
		int defaultFrequency,
		int maxFrequencyGap,
		Set<Integer> admissibleFrequencies,
		Map<Integer, Integer> processingPower
		) throws Exception
	{
		super() ;

		// Preconditions
		assert	numberOfCores > 0 ;
		assert	admissibleFrequencies != null ;
		boolean allPositive = true ;
		for(int f : admissibleFrequencies) {
			allPositive = allPositive && (f > 0) ;
		}
		assert	allPositive ;
		assert	admissibleFrequencies.contains(defaultFrequency) ;
		int max = -1 ;
		for(int f : admissibleFrequencies) {
			if (max < f) {
				max = f ;
			}
		}
		assert	maxFrequencyGap <= max ;
		assert	processingPower.keySet().containsAll(admissibleFrequencies) ;

		this.timestamp = System.currentTimeMillis() ;
		this.timestamperIP = InetAddress.getLocalHost().getHostAddress() ;
		this.numberOfCores = numberOfCores ;
		this.defaultFrequency = defaultFrequency ;
		this.maxFrequencyGap = maxFrequencyGap ;
		this.admissibleFrequencies =
			new HashSet<Integer>(admissibleFrequencies.size()) ;
		for(int f : admissibleFrequencies) {
			this.admissibleFrequencies.add(f) ;
		}
		this.processingPower =
			new HashMap<Integer,Integer>(this.admissibleFrequencies.size()) ;
		for(int f : processingPower.keySet()) {
			this.processingPower.put(f, processingPower.get(f)) ;
		}
	}

	// ------------------------------------------------------------------------
	// Methods
	// ------------------------------------------------------------------------

	/**
	 * @see fr.upmc.datacenter.hardware.processors.interfaces.ProcessorStaticStateI#getTimeStamp()
	 */
	@Override
	public long			getTimeStamp()
	{
		return this.timestamp ;
	}

	/**
	 * @see fr.upmc.datacenter.hardware.processors.interfaces.ProcessorStaticStateI#getTimeStamperId()
	 */
	@Override
	public String		getTimeStamperId()
	{
		return this.timestamperIP ;
	}

	/**
	 * @see fr.upmc.datacenter.hardware.processors.interfaces.ProcessorStaticStateI#getNumberOfCores()
	 */
	@Override
	public int			getNumberOfCores()
	{
		return this.numberOfCores ;
	}

	/**
	 * @see fr.upmc.datacenter.hardware.processors.interfaces.ProcessorStaticStateI#getDefaultFrequency()
	 */
	@Override
	public int			getDefaultFrequency()
	{
		return this.defaultFrequency ;
	}

	/**
	 * @see fr.upmc.datacenter.hardware.processors.interfaces.ProcessorStaticStateI#getMaxFrequencyGap()
	 */
	@Override
	public int			getMaxFrequencyGap()
	{
		return this.maxFrequencyGap ;
	}

	/**
	 * @see fr.upmc.datacenter.hardware.processors.interfaces.ProcessorStaticStateI#getAdmissibleFrequencies()
	 */
	@Override
	public Set<Integer>	getAdmissibleFrequencies()
	{
		return this.admissibleFrequencies ;
	}

	/**
	 * @see fr.upmc.datacenter.hardware.processors.interfaces.ProcessorStaticStateI#getProcessingPower()
	 */
	@Override
	public Map<Integer, Integer>	getProcessingPower()
	{
		return this.processingPower ;
	}
}

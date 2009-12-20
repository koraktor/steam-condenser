<?php
/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2008-2009, Sebastian Staudt
 *
 * @author Sebastian Staudt
 * @license http://www.opensource.org/licenses/bsd-license.php New BSD Lic$
 * @package Steam Condenser (PHP)
 * @subpackage ByteBuffer
 */

require_once "exceptions/BufferUnderflowException.php";

/**
 * @package Steam Condenser (PHP)
 * @subpackage ByteBuffer
 */
class ByteBuffer
{
	/**
	 * @var byte[]
	 */
	private $byteArray;

	/**
	 * @var int
	 */
	private $capacity;

	/**
	 * @var int
	 */
	private $limit;

	/**
	 * @var int
	 */
	private $mark;

	/**
	 * @var int
	 */
	private $position;

	public static function allocate($length)
	{
		return new ByteBuffer(str_repeat("\0", $length));
	}

	public static function wrap($byteArray)
	{
		return new ByteBuffer($byteArray);
	}

	/**
	 * @param byte[] $byteArray
	 */
	public function __construct($byteArray)
	{
		$this->byteArray = $byteArray;
		$this->capacity = strlen($byteArray);
		$this->limit = $this->capacity;
		$this->position = 0;
		$this->mark = -1;
	}

	public function _array()
	{
		return $this->byteArray;
	}

	public function clear()
	{
		$this->limit = $this->capacity;
		$this->position = 0;
		$this->mark = -1;
	}

    public function flip()
    {
        $this->limit = $this->position;
        $this->position = 0;
        $this->mark = -1;

        return $this;
    }

	/**
	 * @param int $length
	 * @return mixed
	 */
	public function get($length = null)
	{
		if($length === null)
		{
			$length = $this->limit - $this->position;
		}
		elseif($length > $this->remaining())
		{
			throw new BufferUnderFlowException();
		}

		$data = substr($this->byteArray, $this->position, $length);
		$this->position += $length;

		if($length < 0)
		{
			debug_print_backtrace();
			die();
		}

		return $data;
	}

	/**
	 * @return byte
	 */
	public function getByte()
	{
		return ord($this->get(1));
	}

	/**
	 * @return float
	 */
	public function getFloat()
	{
		$data = unpack("f", $this->get(4));
		return $data[1];
	}

	/**
	 * @return long
	 */
	public function getLong()
	{
		$data = unpack("l", $this->get(4));
		return $data[1];
	}

	/**
	 * @return short
	 */
	public function getShort()
	{
		$data = unpack("v", $this->get(2));
		return $data[1];
	}

	/**
	 * @return String
	 */
	public function getString()
	{
		$zeroByteIndex = strpos($this->byteArray, "\0", $this->position);
		if($zeroByteIndex === false)
		{
			return "";
		}
		else
		{
			$dataString = $this->get($zeroByteIndex - $this->position);
			$this->position ++;
			return $dataString;
		}
	}

	/**
	 * @return long
	 */
	public function getUnsignedLong()
	{
		$data = unpack("V", $this->get(4));
		return $data[1];
	}

	public function limit($newLimit = null)
	{
		if($newLimit == null)
		{
			return $this->limit;
		}
		else
		{
			$this->limit = $newLimit;
		}
	}

	public function position()
	{
		return $this->position;
	}

	public function put($sourceByteArray)
	{
		$newPosition = min($this->remaining(), strlen($sourceByteArray));
		$this->byteArray = substr_replace($this->byteArray, $sourceByteArray, $this->position, $newPosition);
		$this->position = $newPosition;

		return $this;
	}

	public function remaining()
	{
		return $this->limit - $this->position;
	}

	public function rewind()
	{
		$this->mark = -1;
		$this->position = 0;

		return $this;
	}
}
?>

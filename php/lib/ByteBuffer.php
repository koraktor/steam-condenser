<?php
/**
 * @author Sebastian Staudt
 * @license http://www.opensource.org/licenses/bsd-license.php Modified BSD Lic$
 * @package Steam Interface Package (PHP)
 * @subpackage ByteBuffer
 * @version $Id$
 */

/**
 * @package Steam Interface Package (PHP)
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
  private $pointer;

  /**
   * @param byte[] $byteArray
   */
  public function __construct($byteArray)
  {
    $this->byteArray = $byteArray;
    $this->pointer = 0;
  }
  
  /**
   * @param int $length
   * @return mixed
   */
  public function get($length)
  {
    $data = substr($this->byteArray, $this->pointer, $length);
    $this->pointer += $length;
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
    return $this->get(strpos($this->byteArray, "\0", $this->pointer) - $this->pointer + 1);
  }
}
?>

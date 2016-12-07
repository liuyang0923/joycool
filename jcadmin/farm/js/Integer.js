function Integer(num){
 private:
 this.value=parseInt(num);
 public:

 this.intValue=intValue;
 function intValue(){
  return this.value;
 }

 this.toString=toString;
 function toString(){
  return ""+this.value;
 }

 this.hashCode=hashCode;
 function hashCode(){
  return this.value;
 }

 this.equals=equals;
 function equals(obj){
  return this.intValue()==obj.intValue();
 }
 
 this.compareTo=compareTo;
 function compareTo(obj){
  var i=this.value-obj.value;
  return (i==0)?0:(i>0)?1:-1;
 }
}
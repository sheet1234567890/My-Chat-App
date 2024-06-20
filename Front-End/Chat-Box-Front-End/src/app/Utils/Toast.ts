import Swal from "sweetalert2";

const Toasts = Swal.mixin({
    toast:true,
    position:'top-end',
    showConfirmButton: false,
    timer: 1500,
    timerProgressBar: true,
  })

  export default Toasts
<template>
  <i-frame :src="url" />
</template>
<script>
import iFrame from "@/components/IFrame/index";
export default {
  name: "Swagger",
  components: { iFrame },
  data() {
    return {
      url: process.env.VUE_APP_BASE_API + "/swagger"
    };
  },
};
</script>

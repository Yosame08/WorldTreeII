<script setup>
import {onMounted, ref} from "vue";
import TaskInfo from "@/components/TaskInfo.vue";

const called = ref(false);

const text = ref("又是一个晚上，和几天前一样，你在睡前打开了复旦校园论坛，尝试浏览一些有趣的信息。只不过，这次，你已经知道了蒸旦宇宙的大危机，是在有目的地寻找可能有助于解决危机的线索。最近遇到的几次意外事件和几个奇怪的谜题都显示，蒸旦宇宙的危机不一定是一场大阴谋，可能仅仅是幕后黑手图一乐而制作出来的一场闹剧。`" +
    "[我]只是为了开心就拉着整个学校的人陪自己玩耍，乃至于影响到别的宇宙吗。虽然很任性，但这听起来也太酷了吧。`" +
    "[我]话说......我竟然也有当上救世主、大英雄的一天呢。虽然现在身边没有任何人知道就是了。不过，那所谓的幕后黑手，搞出了这么大的事情，可能也和我一样在不知道哪个宇宙中正常生活着，身边的人都不知道自己竟然干了这么大的坏事。这么一看，这个幕后黑手或许和我现在的心境有些相似吧？" +
    "`非常突然地，破界者的声音在你脑海中响起。`" +
    "[破界者]（我们侦探事务所已经对这次危机的起因有了重大突破！蒸旦大学的内卷危机确实是来自一个幕后黑手，而且我们终于找到幕后黑手是谁了！）`" +
    "[我]（居然这么快吗？按理来说，这种幕后黑手应该是隐藏自己的身份，不到最后一刻绝不暴露的。）" +
    "[破界者]（不如说我们一开始就错了。幕后黑手从来没有过隐藏自己身份的想法。我们遇到的每个怪事、每个谜题都显而易见地揭示着幕后黑手的名字——）`" +
    "——一瞬间，你感觉的你的意识模糊了起来，脑海中响起的破界者的声音仿佛坠入水中一般，朦朦胧胧的听不真切。但你又好像记得你明明完全听清楚了破界者说出的幕后黑手的名字。究竟是哪几个字来着？你理应完全记得——`" +
    "——乐逝大魔王。`" +
    "你的意识更加模糊了。破界者好像正在急切地呼唤你，但你完全听不清他到底在说什么。你好像明白了一切，又好像什么也不知道。恍惚间，你感觉到你好像触碰到了宇宙之间的障壁一般......`" +
    "`......`" +
    "`暑假时光转瞬即逝，你又回到了这个熟悉而又有些陌生的复旦校园。像往常一样，你在睡前打开了复旦校园论坛，随便浏览一些有趣的帖子。`" +
    "`最近，许多帖子都提到校园里出现了怪事，但你向来不相信这种耸人听闻的谣言。还没有往下翻几条，你的目光便被一个看起来很奇怪的帖子吸引住了：`" +
    "`标题：我们的世界遇到了巨大危机，急需帮助！`" +
    "`[卷了也白卷]同学们好，我是来自蒸旦大学（在你们这边的名字似乎叫复旦大学？）的一名学生。最近不知道是因为什么原因，蒸旦大学的学生们突然变卷了！不仅各种水课的教室挤满了旁听的学生，各种课程pre更是堪比联合国大会。本来我和我的好朋友经常联机玩游戏，但这学期他再也没打开过电脑里的蒸菜游戏平台，还以我不认真准备一个月后的小组pre为由将我踢出了小组！这简直太奇怪了，我的印象中去年的蒸旦大学不是这样，大学生也不应该是这样的！" +
    "`[卷了也白卷]我正想打开蒸旦大学的“蛋挞”论坛寻找和我一样发现异样的同学，却没想到有这么多同学不在卷而在论坛中愉快聊天。我仔细看了一会之后才发现，你们并不是蒸旦大学的学生，而是来自一个非常相似的“复旦大学”。复旦大学有个很高的双子楼“光华楼”，我们蒸旦大学也有一个几乎一样的“滑蛋”双子楼，简直太巧了，可我一查全世界都没有“复旦”这个名字的学校！我大胆的猜测，复旦大学和蒸旦大学可能是平行宇宙的一体两面，某种神秘超自然力量让我联系到了复旦宇宙的“蛋挞”论坛，也导致了蒸旦宇宙的内卷异变。最近你们这边是不是也出现了许多怪事？希望同学们能提供一些自己遇到的怪事，其中或许就包含着解决危机的关键线索！" +
    "`[路人A]什么玩意他这是？科幻小说看多了？" +
    "`[路人B]听起来有点扯，但是我感觉最近真挺奇怪的，论坛里各种各样的怪事分享比以前多多了。`" +
    "......`" +
    "`" +
    "你看着看着，顿时觉得身体有点难受，可能入秋感冒了？突然，你感觉仿佛对这个奇怪的帖子有一些印象。居然对这么离谱的东西都能有印象，做梦做多了吧？你不再多想，关上手机，倒头就沉入了梦乡。`" +
    "`" +
    "[???]喂喂，听得见吗？`早上，你被一个熟悉的声音叫醒。`" +
    "[我]等等，我不是在寝室里吗？室友今天都有早八，是谁在叫我？`你猛地从床上坐起来，寝室里空空如也，只有你一个人。`" +
    "[???]终于联系上了！你昨天已经看过我写的帖子了对吧？就是那个蒸旦宇宙内卷危机的帖子。`熟悉的声音继续响起。你再度环顾四周，发现这个声音竟然是从你脑海里凭空响起的！`" +
    "[我]你是谁？你的意思是那个蒸旦宇宙的帖子是真的？你是蒸旦宇宙的人？`你试探性地说。`" +
    "[???]我就是你自己，平行宇宙中对应的你本人，你可以称呼我为‘破界者’。蒸旦宇宙的危机已经影响到了时空的稳定性，所以我可以在蒸旦宇宙中进入复旦的论坛，也能同位连接到你的意识。现在我们非常需要你的帮助！`" +
    "你仔细一听，这个凭空响起的声音确实和你自己的声音完全相同，让你不由得相信了他的话。`[我]那我该怎么帮你呢？`" +
    "[破界者]我和许多志同道合的同伴已经对蒸旦宇宙的危机有了初步的研究，复旦大学中每个真实发生的校园怪谈应该都是被我们宇宙影响的结果。如果能够找到每个怪谈发生的背后原因，或许就能找到解决危机的线索！" +
    "`突然之间，你对危机背后的原因有了一种猜测。虽然没什么来由，但是你迫不及待的向破界者分享了你的看法：`" +
    "[我]等等。有没有可能，你们蒸旦宇宙的危机完全是由某个幕后黑手一手制造出来的？然后我们把这个幕后黑手揪出来就万事大吉了呗。`" +
    "[破界者]你的猜测没什么根据啊。费了这么大心思搞了个内卷危机出来，究竟有什么企图呢？当然，也不能排除这种可能就是了。`" +
    "[我]内卷危机完全就是超自然现象了吧！说不定幕后黑手搞出这种事其实很轻松，而且他的目的单纯就是为了好玩呢？`" +
    "[破界者]......你可以暂且保留你的意见。");
const discussions = ref([]);

const parseTaskDescription = () => {
  discussions.value = text.value.split('`').map((item) => {
    const match = item.match(/^\[(.*?)\](.*)$/);
    if (match) {
      return {user: match[1], content: match[2]};
    } else {
      return {user: null, content: item};
    }
  });
};

onMounted(() => {
  parseTaskDescription();
});

</script>

<template>
  <div>
    <task-info :v-if="called" :discussions="discussions" />
  </div>
</template>

<style>
.el-table .special-row {
  --el-table-tr-bg-color: #ffeba8;
}
</style>
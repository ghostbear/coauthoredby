<script>
	export let username;
	$: snippet = `Co-authored-by: ${username} <${username}@users.noreply.github.com>`

	function copyToClipboard() {
		navigator.clipboard.writeText(snippet)
	}
</script>

<main>
	<h1>Co-authored</h1>
	<p>Simple tool to give credit to users who help you with a commit on GitHub</p>
	<br>
	<b>What is the username of the co-author?</b>
	<div>
		<input bind:value={username} />	
	</div>
	
	{#if username}
		<code id="co-author" on:click={copyToClipboard}>
			{snippet}
		</code>
	{/if}
</main>

<style>
	main {
		text-align: center;
		padding: 1em;
		max-width: 240px;
		margin: 0 auto;
	}

	h1 {
		color: #ff3e00;
		text-transform: uppercase;
		font-size: 4em;
		font-weight: 100;
	}

	@media (min-width: 640px) {
		main {
			max-width: none;
		}
	}
</style>
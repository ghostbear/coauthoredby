import App from './App.svelte';

const app = new App({
	target: document.body,
	props: {
		username: 'ghost'
	}
});

export default app;
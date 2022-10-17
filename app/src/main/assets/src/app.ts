interface PMOptions {
    get?: string[];
    match?: string[];
}
declare function getApkInfo(file: string, options?: PMOptions): any;
declare function getAppName(packageName: string): string | null;
declare function getPackageName(targetAppName: string): string | null;
declare function launch(packageName: string): boolean;
declare function launchApp(targetAppName: string): boolean;
